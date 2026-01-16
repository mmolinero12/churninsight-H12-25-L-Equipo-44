package org.hackaton.oracle.churninsight.web.dto.usuario;

import jakarta.validation.Valid;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosActualizacionDireccion;

public record DatosActualizacionUsuario(
        Long id, String telefono,
        @Valid DatosActualizacionDireccion direccion
) {
}
