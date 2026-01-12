package org.hackaton.oracle.churninsight.web.dto.ml;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudPrediccionIndividualRequest {

    @NotNull(message = "idModelo es obligatorio")
    private Long idModelo;

    @NotNull(message = "idModelo es obligatorio")
    private String descripcion;

    @NotNull(message = "requestJson es obligatorio")
    private JsonNode requestJson;



}
