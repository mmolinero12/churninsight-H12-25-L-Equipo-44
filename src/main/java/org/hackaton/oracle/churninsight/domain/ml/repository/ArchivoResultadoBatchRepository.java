package org.hackaton.oracle.churninsight.domain.ml.repository;

import org.hackaton.oracle.churninsight.domain.ml.entity.ArchivoResultadoBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchivoResultadoBatchRepository
        extends JpaRepository<ArchivoResultadoBatch, Long>{

    Optional<ArchivoResultadoBatch> findBySolicitudBatch(
            SolicitudPrediccionBatch solicitudBatch
    );

}
