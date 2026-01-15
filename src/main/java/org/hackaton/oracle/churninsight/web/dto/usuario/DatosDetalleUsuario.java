package org.hackaton.oracle.churninsight.web.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.shared.enums.sexo.Sexo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DatosDetalleUsuario(

        Long id,
        String nombre,
        @JsonProperty("apellido_paterno")
        String apellidoPaterno,
        @JsonProperty("apellido_materno")
        String apellidoMaterno,
        @JsonProperty("fecha_nacimiento")
        LocalDate fechaNacimiento,
        Sexo sexo,
        String curp,
        String email,
        String telefono,
        Direccion direccion,
        @JsonProperty("fecha_ingreso")
        LocalDateTime fechaIngreso

) {
}
