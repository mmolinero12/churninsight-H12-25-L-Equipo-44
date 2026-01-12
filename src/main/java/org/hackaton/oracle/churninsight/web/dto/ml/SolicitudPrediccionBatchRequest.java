package org.hackaton.oracle.churninsight.web.dto.ml;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolicitudPrediccionBatchRequest {
    @NotNull
    private Long idModelo;

    @NotNull
    private Long idUsuario;

    @NotBlank
    private String descripcion;

}
