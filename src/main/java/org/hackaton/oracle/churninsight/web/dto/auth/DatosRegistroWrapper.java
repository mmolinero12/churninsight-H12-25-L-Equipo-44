package org.hackaton.oracle.churninsight.web.dto.auth;

import org.hackaton.oracle.churninsight.web.dto.usuario.DatosRegistroUsuario;

public record DatosRegistroWrapper(
        DatosRegistroUsuario usuario,
        DatosRegistroCredenciales credenciales
) {}
