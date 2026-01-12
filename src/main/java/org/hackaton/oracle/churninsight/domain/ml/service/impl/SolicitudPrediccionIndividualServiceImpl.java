package org.hackaton.oracle.churninsight.domain.ml.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.ml.service.ResultadoPrediccionService;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.repository.ModeloRepository;
import org.hackaton.oracle.churninsight.domain.ml.repository.SolicitudPrediccionIndividualRepository;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionIndividualService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "mlOperationsTransactionManager")
public class SolicitudPrediccionIndividualServiceImpl
        implements SolicitudPrediccionIndividualService{

    private final ModeloRepository modeloRepository;
    private final SolicitudPrediccionIndividualRepository solicitudRepository;
    private final ResultadoPrediccionService resultadoPrediccionService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public SolicitudPrediccionIndividual crearSolicitud(
            Long idModelo,
            Long idUsuario,
            String descripcion,
            JsonNode requestJson
    ) {

        Modelo modelo = modeloRepository.findById(idModelo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Modelo no encontrado"));


        // Los services NO leen flags directamente
        // Preguntan al dominio
        if (!modelo.soportaPrediccionIndividual()) {
            throw new IllegalStateException(
                    "El modelo no soporta predicciones individuales"
            );
        }


        SolicitudPrediccionIndividual solicitud =
                SolicitudPrediccionIndividual.builder()
                        .modelo(modelo)
                        .idUsuario(idUsuario)
                        .descripcion(descripcion)
                        .estado(EstadoPrediccion.RECIBIDA)
                        .requestJson(requestJson)
                        .fechaSolicitud(OffsetDateTime.now())
                        .build();

        solicitud = solicitudRepository.save(solicitud);

        // LO QUE FALTABA de JAVA a PYTHON

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> httpEntity =
                    new HttpEntity<>(requestJson.toString(), headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            modelo.getEndpointIndividual(),
                            httpEntity,
                            String.class
                    );


            JsonNode respuestaPython =
                    objectMapper.readTree(response.getBody());

            resultadoPrediccionService.guardarResultadoIndividual(
                    solicitud.getId(),
                    respuestaPython
            );

            solicitud.marcarComoCompletada();
            solicitud.marcarFechaRespuesta();
        } catch (JsonProcessingException ex) {

            solicitud.marcarComoError("Respuesta inválida del servicio ML");
            solicitud.marcarFechaRespuesta();

        } catch (ResourceAccessException ex) {

            solicitud.marcarComoError(
                    "No fue posible conectar con el servicio de predicción ML"
            );
            solicitud.marcarFechaRespuesta();

        } catch (HttpStatusCodeException ex) {

            solicitud.marcarComoError(
                    "Error devuelto por el servicio de predicción ML: HTTP "
                            + ex.getStatusCode()
            );
            solicitud.marcarFechaRespuesta();
        }

        return solicitud;
    }

    @Override
    public SolicitudPrediccionIndividual marcarComoProcesando(Long idSolicitud) {
        SolicitudPrediccionIndividual solicitud = obtener(idSolicitud);
        solicitud.marcarComoProcesando();
        return solicitud;
    }

    @Override
    public SolicitudPrediccionIndividual marcarComoCompletada(Long idSolicitud) {
        SolicitudPrediccionIndividual solicitud = obtener(idSolicitud);
        solicitud.marcarComoCompletada();
        solicitud.marcarFechaRespuesta();
        return solicitud;
    }

    @Override
    public SolicitudPrediccionIndividual marcarComoError(
            Long idSolicitud,
            String mensajeError
    ) {
        SolicitudPrediccionIndividual solicitud = obtener(idSolicitud);
        solicitud.marcarComoError(mensajeError);
        solicitud.marcarFechaRespuesta();
        return solicitud;
    }

    private SolicitudPrediccionIndividual obtener(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud)
                .orElseThrow(() ->
                        new IllegalArgumentException("Solicitud no encontrada"));
    }


    @Override
    public Page<SolicitudPrediccionIndividual> listar(Pageable pageable) {
        return solicitudRepository.findAll(pageable);
    }

    @Override
    public SolicitudPrediccionIndividual obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Predicción individual no encontrada con id " + id
                        )
                );
    }


}
