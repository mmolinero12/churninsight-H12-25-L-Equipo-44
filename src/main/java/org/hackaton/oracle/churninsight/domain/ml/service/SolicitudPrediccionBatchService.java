package org.hackaton.oracle.churninsight.domain.ml.service;

import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudPrediccionBatchService{

    SolicitudPrediccionBatch crearSolicitud(
            Long idModelo,
            Long idUsuario,
            String descripcion,
            byte[] inputCsv,
            String inputCsvName
    );

    SolicitudPrediccionBatch marcarComoProcesando(Long idSolicitud);

    SolicitudPrediccionBatch marcarComoCompletada(Long idSolicitud);

    SolicitudPrediccionBatch marcarComoError(
            Long idSolicitud,
            String mensajeError
    );

    Page<SolicitudPrediccionBatch> listar(Pageable pageable);

    SolicitudPrediccionBatch obtenerPorId(Long id);

}
