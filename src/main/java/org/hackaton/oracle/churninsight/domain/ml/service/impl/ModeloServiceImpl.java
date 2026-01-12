package org.hackaton.oracle.churninsight.domain.ml.service.impl;

import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoModelo;
import org.hackaton.oracle.churninsight.domain.ml.repository.ModeloRepository;
import org.hackaton.oracle.churninsight.domain.ml.service.ModeloService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "mlTransactionManager")
public class ModeloServiceImpl implements ModeloService {

    private final ModeloRepository modeloRepository;

    @Override
    public Modelo crearModelo(Modelo modelo) {
        boolean existe = modeloRepository.existsByNombreAndVersion(
                modelo.getNombre(),
                modelo.getVersion()
        );

        if (existe) {
            throw new IllegalStateException(
                    "Ya existe un modelo con el mismo nombre y versión"
            );
        }

        Modelo modeloPersistir = Modelo.builder()
                .nombre(modelo.getNombre())
                .version(modelo.getVersion())
                .descripcion(modelo.getDescripcion())
                .estado(modelo.getEstado())
                .soportaIndividual(modelo.getSoportaIndividual())
                .soportaBatch(modelo.getSoportaBatch())
                .endpointIndividual(modelo.getEndpointIndividual())
                .endpointBatch(modelo.getEndpointBatch())
                .fechaCreacion(OffsetDateTime.now())
                .build();

        return modeloRepository.save(modeloPersistir);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Modelo> listarModelos(Pageable pageable) {
        return modeloRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Modelo> listarModelosPorEstado(
            EstadoModelo estado,
            Pageable pageable
    ) {
        return modeloRepository.findByEstado(estado, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Modelo obtenerPorId(Long id) {
        return modeloRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Modelo no encontrado"));
    }

    @Override
    public Modelo cambiarEstado(Long idModelo, EstadoModelo nuevoEstado) {
        Modelo modelo = obtenerPorId(idModelo);
        modelo.cambiarEstado(nuevoEstado); // ⚠️ ojo aquí
        return modelo;
    }

}
