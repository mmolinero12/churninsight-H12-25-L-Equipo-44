package org.hackaton.oracle.churninsight.domain.ml.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ResultadoPrediccionService {

    ResultadoPrediccion guardarResultadoIndividual(
            Long idSolicitudIndividual,
            JsonNode resultadoJson
    );

    ResultadoPrediccion guardarResultadoBatch(
            Long idSolicitudBatch,
            JsonNode resultadoJson
    );

    Optional<ResultadoPrediccion> obtenerPorSolicitudIndividual(Long idSolicitud);


    Page<ResultadoPrediccion> listar(Pageable pageable);

    ResultadoPrediccion obtenerPorId(Long id);


}
