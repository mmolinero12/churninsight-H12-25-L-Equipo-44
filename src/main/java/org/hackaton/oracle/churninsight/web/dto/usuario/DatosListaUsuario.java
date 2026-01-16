package org.hackaton.oracle.churninsight.web.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.usuario.entity.Usuario;

public record DatosListaUsuario(

        Long id,
        String nombre,
        @JsonProperty("apellido_paterno")
        String apellidoPaterno,
        @JsonProperty("apellido_materno")
        String apellidoMaterno,
        String email,
        String telefono,
        Direccion direccion

) {
}
