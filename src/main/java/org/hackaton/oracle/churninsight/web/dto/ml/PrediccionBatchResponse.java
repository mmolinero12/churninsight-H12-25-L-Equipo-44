package org.hackaton.oracle.churninsight.web.dto.ml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PrediccionBatchResponse {

    private Long idSolicitud;
    private EstadoPrediccion estado;
    private OffsetDateTime fechaSolicitud;
    private OffsetDateTime fechaRespuesta;
    private String mensajeError;


    public static PrediccionBatchResponse fromEntity(
            SolicitudPrediccionBatch s
    ) {
        return new PrediccionBatchResponse(
                s.getId(),
                s.getEstado(),
                s.getFechaSolicitud(),
                s.getFechaRespuesta(),
                s.getMensajeError()
        );
    }


}
