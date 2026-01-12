package org.hackaton.oracle.churninsight.web.dto.ml;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PrediccionIndividualResponse {

    private Long idSolicitud;
    private EstadoPrediccion estado;
    private OffsetDateTime fechaSolicitud;
    private OffsetDateTime fechaRespuesta;
    private JsonNode resultado;

    public static PrediccionIndividualResponse fromEntity(
            SolicitudPrediccionIndividual s,
            ResultadoPrediccion resultado
    ) {
        return new PrediccionIndividualResponse(
                s.getId(),
                s.getEstado(),
                s.getFechaSolicitud(),
                s.getFechaRespuesta(),
                resultado != null ? resultado.getResultadoJson() : null
        );
    }


}
