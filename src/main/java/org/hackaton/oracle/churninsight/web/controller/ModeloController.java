package org.hackaton.oracle.churninsight.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoModelo;
import org.hackaton.oracle.churninsight.domain.ml.service.ModeloService;
import org.hackaton.oracle.churninsight.web.dto.ml.CrearModeloRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modelos")
@RequiredArgsConstructor
@Validated
public class ModeloController {

    private final ModeloService modeloService;

    // -------------------------
    // CREAR MODELO
    // -------------------------
    @PostMapping
    public ResponseEntity<Long> crearModelo(
            @Valid @RequestBody CrearModeloRequest request
    ) {

        Modelo modelo = Modelo.builder()
                .nombre(request.getNombre())
                .version(request.getVersion())
                .descripcion(request.getDescripcion())
                .soportaIndividual(request.getSoportaIndividual())
                .soportaBatch(request.getSoportaBatch())
                .endpointIndividual(request.getEndpointIndividual())
                .endpointBatch(request.getEndpointBatch())
                .idUsuario(request.getIdUsuario())
                .estado(EstadoModelo.ACTIVO)
                .build();

        Modelo creado = modeloService.crearModelo(modelo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado.getId());
    }

    // -------------------------
    // LISTAR MODELOS (dashboard)
    // -------------------------
    @GetMapping
    public Page<Modelo> listarModelos(
            @RequestParam(required = false) EstadoModelo estado,
            Pageable pageable
    ) {
        if (estado != null) {
            return modeloService.listarModelosPorEstado(estado, pageable);
        }
        return modeloService.listarModelos(pageable);
    }

    // -------------------------
    // OBTENER POR ID
    // -------------------------
    @GetMapping("/{id}")
    public Modelo obtenerPorId(@PathVariable Long id) {
        return modeloService.obtenerPorId(id);
    }

    // -------------------------
    // CAMBIAR ESTADO
    // -------------------------
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoModelo estado
    ) {
        modeloService.cambiarEstado(id, estado);
        return ResponseEntity.noContent().build();
    }
}
