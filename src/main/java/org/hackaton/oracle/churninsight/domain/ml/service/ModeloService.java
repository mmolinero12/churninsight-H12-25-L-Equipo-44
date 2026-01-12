package org.hackaton.oracle.churninsight.domain.ml.service;

import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoModelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModeloService {

    Modelo crearModelo(Modelo modelo);

    Page<Modelo> listarModelos(Pageable pageable);

    Page<Modelo> listarModelosPorEstado(EstadoModelo estado, Pageable pageable);

    Modelo obtenerPorId(Long id);

    Modelo cambiarEstado(Long idModelo, EstadoModelo nuevoEstado);
}
