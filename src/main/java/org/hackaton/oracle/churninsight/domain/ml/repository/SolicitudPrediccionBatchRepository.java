package org.hackaton.oracle.churninsight.domain.ml.repository;

import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudPrediccionBatchRepository
        extends JpaRepository<SolicitudPrediccionBatch, Long>{

    Page<SolicitudPrediccionBatch> findByEstado(
            EstadoPrediccion estado,
            Pageable pageable
    );

    Page<SolicitudPrediccionBatch> findByModelo(
            Modelo modelo,
            Pageable pageable
    );

    Page<SolicitudPrediccionBatch> findByModeloAndEstado(
            Modelo modelo,
            EstadoPrediccion estado,
            Pageable pageable
    );


}
