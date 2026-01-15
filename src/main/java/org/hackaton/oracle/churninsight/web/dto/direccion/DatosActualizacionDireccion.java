package org.hackaton.oracle.churninsight.web.dto.direccion;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
/**
 * Campos null no se actualizan.
 * Strings vacíos se ignoran.
 */
public record DatosActualizacionDireccion(

        // Indica que el JSON key "calle_numero" mapea a esta variable camelCase
        @JsonProperty("calle_numero")
        String calleNumero,
        String colonia,
        // Indica que el JSON key "alcaldia_municipio" mapea a esta variable camelCase
        @JsonProperty("alcaldia_municipio")
        String alcaldiaMunicipio,
        String ciudad,
        String estado,
        // @JsonProperty("codigo_postal")
        @Pattern(regexp = "\\d{5}")  String codigoPostal

) {
}
