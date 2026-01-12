package org.hackaton.oracle.churninsight.domain.ml.repository;

import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultadoPrediccionRepository
        extends JpaRepository<ResultadoPrediccion, Long>{


    Optional<ResultadoPrediccion> findBySolicitudIndividual(
            SolicitudPrediccionIndividual solicitudIndividual
    );

    Optional<ResultadoPrediccion> findBySolicitudBatch(
            SolicitudPrediccionBatch solicitudBatch
    );

}
