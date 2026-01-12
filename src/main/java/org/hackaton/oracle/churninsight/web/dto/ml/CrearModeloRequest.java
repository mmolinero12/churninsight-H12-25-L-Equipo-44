package org.hackaton.oracle.churninsight.web.dto.ml;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CrearModeloRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String version;

    private String descripcion;

    @NotNull
    private Boolean soportaIndividual;

    @NotNull
    private Boolean soportaBatch;

    private String endpointIndividual;

    private String endpointBatch;

    @NotNull
    private Long idUsuario;




}
