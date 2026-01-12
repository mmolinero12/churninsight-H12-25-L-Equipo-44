package org.hackaton.oracle.churninsight.domain.ml.repository;

import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudPrediccionIndividualRepository
        extends JpaRepository<SolicitudPrediccionIndividual, Long>{

    Page<SolicitudPrediccionIndividual> findByEstado(
            EstadoPrediccion estado,
            Pageable pageable
    );

    Page<SolicitudPrediccionIndividual> findByModelo(
            Modelo modelo,
            Pageable pageable
    );

    Page<SolicitudPrediccionIndividual> findByModeloAndEstado(
            Modelo modelo,
            EstadoPrediccion estado,
            Pageable pageable
    );

}
