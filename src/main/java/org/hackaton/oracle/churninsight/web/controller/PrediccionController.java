package org.hackaton.oracle.churninsight.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionBatchService;
import org.hackaton.oracle.churninsight.domain.ml.service.SolicitudPrediccionIndividualService;
import org.hackaton.oracle.churninsight.web.dto.ml.SolicitudPrediccionBatchRequest;
import org.hackaton.oracle.churninsight.web.dto.ml.SolicitudPrediccionIndividualRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/predicciones")
@RequiredArgsConstructor
@Validated
public class PrediccionController {

    private final SolicitudPrediccionIndividualService individualService;
    private final SolicitudPrediccionBatchService batchService;

    // -------------------------
    // PREDICCIÓN INDIVIDUAL
    // -------------------------
    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Long> crearPrediccionIndividual(
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(solicitud.getId());
    }

    // -------------------------
    // PREDICCIÓN BATCH
    // -------------------------
    @PostMapping(
            value = "/batch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Long> crearPrediccionBatch(
            @RequestPart("request")
            @Valid SolicitudPrediccionBatchRequest request,

            @RequestPart("file")
            MultipartFile file
    ) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV es obligatorio");
        }

        SolicitudPrediccionBatch solicitud =
                batchService.crearSolicitud(
                        request.getIdModelo(),
                        request.getIdUsuario(),
                        request.getDescripcion(),
                        obtenerBytes(file),
                        file.getOriginalFilename()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(solicitud.getId());
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
    public ResponseEntity<Page<SolicitudPrediccionIndividual>> listarPrediccionesIndividuales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SolicitudPrediccionIndividual> resultado =
                individualService.listar(PageRequest.of(page, size));

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/individual/{id}")
    public ResponseEntity<SolicitudPrediccionIndividual> obtenerPrediccionIndividual(
            @PathVariable Long id
    ) {
        SolicitudPrediccionIndividual solicitud =
                individualService.obtenerPorId(id);

        return ResponseEntity.ok(solicitud);
    }


// -------------------------
// GET - PREDICCIÓN BATCH
// -------------------------

    @GetMapping("/batch")
    public ResponseEntity<Page<SolicitudPrediccionBatch>> listarPrediccionesBatch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SolicitudPrediccionBatch> resultado =
                batchService.listar(PageRequest.of(page, size));

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/batch/{id}")
    public ResponseEntity<SolicitudPrediccionBatch> obtenerPrediccionBatch(
            @PathVariable Long id
    ) {
        SolicitudPrediccionBatch solicitud =
                batchService.obtenerPorId(id);

        return ResponseEntity.ok(solicitud);
    }


}
