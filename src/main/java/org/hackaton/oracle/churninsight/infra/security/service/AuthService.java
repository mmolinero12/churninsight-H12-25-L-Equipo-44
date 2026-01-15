package org.hackaton.oracle.churninsight.infra.security.service;

import org.hackaton.oracle.churninsight.web.dto.auth.DatosTokenJWT;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosDetalleUsuario;
import org.hackaton.oracle.churninsight.web.dto.auth.LoginRequest;


public interface AuthService {
    // AuthService.java
    void registrarCredenciales(DatosDetalleUsuario usuario, String password);

    DatosTokenJWT autenticar(LoginRequest login);
}
