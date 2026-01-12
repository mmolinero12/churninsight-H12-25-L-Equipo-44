package org.hackaton.oracle.churninsight.web.dto.ml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PrediccionIndividualResumenResponse {

    private Long idSolicitud;
    private EstadoPrediccion estado;
    private OffsetDateTime fechaSolicitud;
    private String descripcion;

    public static PrediccionIndividualResumenResponse fromEntity(
            SolicitudPrediccionIndividual s
    ) {
        return new PrediccionIndividualResumenResponse(
                s.getId(),
                s.getEstado(),
                s.getFechaSolicitud(),
                s.getDescripcion()
        );
    }
}
