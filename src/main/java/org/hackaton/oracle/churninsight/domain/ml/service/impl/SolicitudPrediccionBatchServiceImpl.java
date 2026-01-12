package org.hackaton.oracle.churninsight.domain.ml.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.ArchivoResultadoBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.repository.ArchivoResultadoBatchRepository;
import org.hackaton.oracle.churninsight.domain.ml.repository.ResultadoPrediccionRepository;
import org.hackaton.oracle.churninsight.domain.ml.service.ResultadoPrediccionService;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.repository.ModeloRepository;
import org.hackaton.oracle.churninsight.domain.ml.repository.SolicitudPrediccionBatchRepository;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionBatchService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "mlOperationsTransactionManager")
public class SolicitudPrediccionBatchServiceImpl
        implements SolicitudPrediccionBatchService{

    private final ModeloRepository modeloRepository;
    private final SolicitudPrediccionBatchRepository solicitudRepository;
    private final ArchivoResultadoBatchRepository archivoResultadoBatchRepository;
    private final ResultadoPrediccionRepository resultadoPrediccionRepository;

    private final ResultadoPrediccionService resultadoPrediccionService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public SolicitudPrediccionBatch crearSolicitud(
            Long idModelo,
            Long idUsuario,
            String descripcion,
            byte[] inputCsv,
            String inputCsvName
    ) {

        // 1. Obtener y validar el modelo
        Modelo modelo = modeloRepository.findById(idModelo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Modelo no encontrado"));

        if (!modelo.soportaPrediccionBatch()) {
            throw new IllegalStateException(
                    "El modelo no soporta predicciones batch"
            );
        }

        // 2. Crear y guardar la solicitud batch (AQUÍ se genera el ID)
        SolicitudPrediccionBatch solicitud =
                SolicitudPrediccionBatch.builder()
                        .modelo(modelo)
                        .idUsuario(idUsuario)
                        .descripcion(descripcion)
                        .estado(EstadoPrediccion.RECIBIDA)
                        .inputCsv(inputCsv)
                        .inputCsvName(inputCsvName)
                        .fechaSolicitud(OffsetDateTime.now())
                        .build();

        System.out.println("********** Clase de inputCsv: ");
        System.out.println(inputCsv == null ? "NULL" : inputCsv.getClass());

        solicitud = solicitudRepository.save(solicitud);

        // 3. Llamar al servicio Python de forma síncrona
        try {
            // --- Headers multipart ---
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // --- Body multipart con el CSV ---
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add(
                    "file",
                    new ByteArrayResource(inputCsv) {
                        @Override
                        public String getFilename() {
                            return inputCsvName;
                        }
                    }
            );

            HttpEntity<MultiValueMap<String, Object>> httpEntity =
                    new HttpEntity<>(body, headers);

            // --- POST a Python ---
            ResponseEntity<byte[]> response =
                    restTemplate.postForEntity(
                            modelo.getEndpointBatch(),
                            httpEntity,
                            byte[].class
                    );

            byte[] resultadoCsv = response.getBody();

            if (resultadoCsv == null || resultadoCsv.length == 0) {
                throw new IllegalStateException("El servicio ML devolvió un CSV vacío");
            }

            // 4. Guardar el archivo CSV completo devuelto por Python
            ArchivoResultadoBatch archivoResultado =
                    ArchivoResultadoBatch.builder()
                            .solicitudBatch(solicitud)
                            .resultadoCsv(resultadoCsv)
                            .resultadoCsvName("resultado_batch.csv")
                            .fechaCreacion(OffsetDateTime.now())
                            .build();

            archivoResultadoBatchRepository.save(archivoResultado);

            // 5. Parsear el CSV línea por línea
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(resultadoCsv))
            )) {

                String line;
                boolean header = true;

                while ((line = reader.readLine()) != null) {
                    // Saltar encabezado
                    if (header) {
                        header = false;
                        continue;
                    }

                    // 6. Convertir cada línea a JsonNode
                    // (ejemplo simple: una línea = un campo "resultado")
                    ObjectNode filaJson = objectMapper.createObjectNode();
                    filaJson.put("resultado", line);

                    // 7. Guardar cada fila como ResultadoPrediccion
                    ResultadoPrediccion resultado =
                            ResultadoPrediccion.builder()
                                    .solicitudBatch(solicitud)
                                    .resultadoJson(filaJson)
                                    .fechaCreacion(OffsetDateTime.now())
                                    .build();

                    resultadoPrediccionRepository.save(resultado);
                }
            }

            // 8. Marcar solicitud como completada
            solicitud.marcarComoCompletada();
            solicitud.marcarFechaRespuesta();

        } catch (ResourceAccessException ex) {

            // Python no accesible
            solicitud.marcarComoError(
                    "No fue posible conectar con el servicio de predicción ML"
            );
            solicitud.marcarFechaRespuesta();

        } catch (HttpStatusCodeException ex) {

            // Python respondió error HTTP
            solicitud.marcarComoError(
                    "Error devuelto por el servicio de predicción ML: HTTP "
                            + ex.getStatusCode()
            );
            solicitud.marcarFechaRespuesta();

        } catch (Exception ex) {

            // Cualquier otro error (parseo, IO, etc.)
            solicitud.marcarComoError(
                    "Error inesperado durante la predicción batch"
            );
            solicitud.marcarFechaRespuesta();
        }

        // 9. Retornar la solicitud (Hibernate hace dirty checking)
        return solicitud;
    }



    @Override
    public SolicitudPrediccionBatch marcarComoProcesando(Long idSolicitud) {
        SolicitudPrediccionBatch solicitud = obtener(idSolicitud);
        solicitud.marcarComoProcesando();
        return solicitud;
    }

    @Override
    public SolicitudPrediccionBatch marcarComoCompletada(Long idSolicitud) {
        SolicitudPrediccionBatch solicitud = obtener(idSolicitud);
        solicitud.marcarComoCompletada();
        solicitud.marcarFechaRespuesta();
        return solicitud;
    }

    @Override
    public SolicitudPrediccionBatch marcarComoError(
            Long idSolicitud,
            String mensajeError
    ) {
        SolicitudPrediccionBatch solicitud = obtener(idSolicitud);
        solicitud.marcarComoError(mensajeError);
        solicitud.marcarFechaRespuesta();
        return solicitud;
    }

    private SolicitudPrediccionBatch obtener(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud)
                .orElseThrow(() ->
                        new IllegalArgumentException("Solicitud batch no encontrada"));
    }


    @Override
    public Page<SolicitudPrediccionBatch> listar(Pageable pageable) {
        return solicitudRepository.findAll(pageable);
    }

    @Override
    public SolicitudPrediccionBatch obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Predicción batch no encontrada con id " + id
                        )
                );
    }

}
