package org.hackaton.oracle.churninsight.domain.auth.repository;


import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioAutenticacionRepository extends JpaRepository<UsuarioAutenticacion, Long> {

    @EntityGraph(attributePaths = {
            "usuarioRoles",
            "usuarioRoles.rol"
    })
    Optional<UsuarioAutenticacion> findByUsername(String username);
}
