package org.hackaton.oracle.churninsight.web.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hackaton.oracle.churninsight.domain.shared.enums.sexo.Sexo;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosRegistroDireccion;

import java.time.LocalDate;

// @JsonProperty jackson.annotation Sólo para el DTO que recibe del cliente
// El resto de anotaciones @ son de validation
public record DatosRegistroUsuario(

        @NotBlank String nombre,

        @JsonProperty("apellido_paterno")
        @NotBlank String apellidoPaterno,

        @JsonProperty("apellido_materno")
        @NotBlank String apellidoMaterno,

        @JsonProperty("fecha_nacimiento")
        @Past @NotNull LocalDate fechaNacimiento,     // Para LocalDate deber ser @NotNull

        @NotNull Sexo sexo,                     // Para ENUMS debe ser @NotNull

        @Pattern(
                regexp = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}\\d{2}$",
                message = "CURP inválida"
        )
        @NotBlank String curp,

        @NotBlank @Email String email,

        @NotBlank String telefono,

        @Valid @NotNull DatosRegistroDireccion direccion,

        @JsonProperty("fecha_ingreso")
        @PastOrPresent @NotNull LocalDate fechaIngreso     // Para LocalDate deber ser @NotNull

) {
}
