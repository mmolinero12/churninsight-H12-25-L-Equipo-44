package org.hackaton.oracle.churninsight.domain.auth.repository;


import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {

}
