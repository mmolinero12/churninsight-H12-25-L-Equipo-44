package org.hackaton.oracle.churninsight.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.ml.service.ResultadoPrediccionService;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionBatchService;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionIndividualService;
import org.hackaton.oracle.churninsight.web.dto.ml.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@RestController
@RequestMapping("/api/predicciones")
@RequiredArgsConstructor
@Validated
public class PrediccionController {

    private final SolicitudPrediccionIndividualService individualService;
    private final SolicitudPrediccionBatchService batchService;
    private final ResultadoPrediccionService resultadoPrediccionService;

    // -------------------------
    // PREDICCIÓN INDIVIDUAL
    // -------------------------
    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrediccionIndividualResponse> crearPrediccionIndividual(
            @Valid @RequestBody SolicitudPrediccionIndividualRequest request
    ) {

        // ⚠️ idUsuario hoy lo pasamos fijo / mock
        Long idUsuario = 1L;

        SolicitudPrediccionIndividual solicitud =
                individualService.crearSolicitud(
                        request.getIdModelo(),
                        idUsuario,
                        request.getDescripcion(),
                        request.getRequestJson()
                );

        Optional<ResultadoPrediccion> resultadoOpt =
                resultadoPrediccionService.obtenerPorSolicitudIndividual(
                        solicitud.getId()
                );

        PrediccionIndividualResponse response =
                PrediccionIndividualResponse.fromEntity(
                        solicitud,
                        resultadoOpt.orElse(null)
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    // -------------------------
    // PREDICCIÓN BATCH
    // -------------------------
    @PostMapping(
            value = "/batch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PrediccionBatchResponse> crearPrediccionBatch(
            @RequestPart("request")
            @Valid SolicitudPrediccionBatchRequest request,

            @RequestPart("file")
            MultipartFile file
    ) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV es obligatorio");
        }

        // 1. Crear la solicitud batch (incluye llamada síncrona a Python)
        SolicitudPrediccionBatch solicitud =
                batchService.crearSolicitud(
                        request.getIdModelo(),
                        request.getIdUsuario(),
                        request.getDescripcion(),
                        obtenerBytes(file),
                        file.getOriginalFilename()
                );

        // 2. Construir el DTO de respuesta para Postman
        PrediccionBatchResponse response =
                PrediccionBatchResponse.fromEntity(solicitud);

        // 3. Responder al cliente
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    private byte[] obtenerBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "No se pudo leer el archivo CSV", e
            );
        }
    }

    // -------------------------
// GET - PREDICCIÓN INDIVIDUAL
// -------------------------

    @GetMapping("/individual")
    public ResponseEntity<Page<PrediccionIndividualResumenResponse>> listarPrediccionesIndividuales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SolicitudPrediccionIndividual> solicitudes =
                individualService.listar(PageRequest.of(page, size));

        Page<PrediccionIndividualResumenResponse> response =
                solicitudes.map(PrediccionIndividualResumenResponse::fromEntity);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/individual/{id}")
    public ResponseEntity<PrediccionIndividualResumenResponse> obtenerPrediccionIndividual(
            @PathVariable Long id
    ) {
        SolicitudPrediccionIndividual solicitud =
                individualService.obtenerPorId(id);

        return ResponseEntity.ok(
                PrediccionIndividualResumenResponse.fromEntity(solicitud)
        );
    }


// -------------------------
// GET - PREDICCIÓN BATCH
// -------------------------

    @GetMapping("/batch")
    public ResponseEntity<Page<PrediccionBatchResumenResponse>> listarPrediccionesBatch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SolicitudPrediccionBatch> solicitudes =
                batchService.listar(PageRequest.of(page, size));

        Page<PrediccionBatchResumenResponse> response =
                solicitudes.map(PrediccionBatchResumenResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<PrediccionBatchResumenResponse> obtenerPrediccionBatch(
            @PathVariable Long id
    ) {
        SolicitudPrediccionBatch solicitud =
                batchService.obtenerPorId(id);

        return ResponseEntity.ok(
                PrediccionBatchResumenResponse.fromEntity(solicitud)
        );
    }

    // -------------------------
    // GET - RESULTADOS
    // -------------------------

    @GetMapping("/resultados")
    public ResponseEntity<Page<ResultadoPrediccionResponse>> listarResultados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ResultadoPrediccionResponse> response =
                resultadoPrediccionService
                        .listar(PageRequest.of(page, size))
                        .map(ResultadoPrediccionResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    // -------------------------
    // GET - RESULTADO POR ID
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPrediccionResponse> obtenerResultado(
            @PathVariable Long id
    ) {
        ResultadoPrediccion resultado =
                resultadoPrediccionService.obtenerPorId(id);

        return ResponseEntity.ok(
                ResultadoPrediccionResponse.fromEntity(resultado)
        );
    }




}
