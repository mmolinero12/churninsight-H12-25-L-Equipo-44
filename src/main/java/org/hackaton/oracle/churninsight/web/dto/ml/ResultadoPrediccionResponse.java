package org.hackaton.oracle.churninsight.web.dto.ml;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ResultadoPrediccionResponse {

    private Long id;
    private Long idSolicitudIndividual;
    private Long idSolicitudBatch;
    private JsonNode resultadoJson;
    private OffsetDateTime fechaCreacion;

    public static ResultadoPrediccionResponse fromEntity(
            ResultadoPrediccion r
    ) {
        return new ResultadoPrediccionResponse(
                r.getId(),
                r.getSolicitudIndividual() != null
                        ? r.getSolicitudIndividual().getId()
                        : null,
                r.getSolicitudBatch() != null
                        ? r.getSolicitudBatch().getId()
                        : null,
                r.getResultadoJson(),
                r.getFechaCreacion()
        );
    }
}
