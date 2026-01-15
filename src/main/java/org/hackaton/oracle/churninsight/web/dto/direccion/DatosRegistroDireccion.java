package org.hackaton.oracle.churninsight.web.dto.direccion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroDireccion(

        // Indica que el JSON key "calle_y_numero" mapea a esta variable camelCase
        @JsonProperty("calle_numero")
        @NotBlank String calleNumero,
        @NotBlank  String colonia,
        // Indica que el JSON key "alcaldia_municipio" mapea a esta variable camelCase
        @JsonProperty("alcaldia_municipio")
        @NotBlank  String alcaldiaMunicipio,
        @NotBlank  String ciudad,
        @NotBlank  String estado,
        @JsonProperty("codigo_postal")
        @NotBlank @Pattern(regexp = "\\d{5}")  String codigoPostal

) {
}
