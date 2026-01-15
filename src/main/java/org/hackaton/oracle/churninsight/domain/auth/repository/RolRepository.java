package org.hackaton.oracle.churninsight.domain.auth.repository;


import org.hackaton.oracle.churninsight.domain.auth.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRol(String userRol);
}
