package org.hackaton.oracle.churninsight.domain.ml.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;

public interface ResultadoPrediccionService {

    ResultadoPrediccion guardarResultadoIndividual(
            Long idSolicitudIndividual,
            JsonNode resultadoJson
    );

    ResultadoPrediccion guardarResultadoBatch(
            Long idSolicitudBatch,
            JsonNode resultadoJson
    );


}
