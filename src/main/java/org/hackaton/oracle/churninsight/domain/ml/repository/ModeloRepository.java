package org.hackaton.oracle.churninsight.domain.ml.repository;

import org.hackaton.oracle.churninsight.domain.ml.entity.Modelo;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoModelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeloRepository
        extends JpaRepository<Modelo, Long>{

    Page<Modelo> findByEstado(EstadoModelo estado, Pageable pageable);

    Page<Modelo> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Optional<Modelo> findByNombreAndVersion(String nombre, String version);

    boolean existsByNombreAndVersion(String nombre, String version);


}
