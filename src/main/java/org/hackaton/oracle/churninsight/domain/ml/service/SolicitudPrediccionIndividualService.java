package org.hackaton.oracle.churninsight.domain.ml.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SolicitudPrediccionIndividualService{

    SolicitudPrediccionIndividual crearSolicitud(
            Long idModelo,
            Long idUsuario,
            String descripcion,
            JsonNode requestJson
    );

    SolicitudPrediccionIndividual marcarComoProcesando(Long idSolicitud);

    SolicitudPrediccionIndividual marcarComoCompletada(Long idSolicitud);

    SolicitudPrediccionIndividual marcarComoError(
            Long idSolicitud,
            String mensajeError
    );

    Page<SolicitudPrediccionIndividual> listar(Pageable pageable);

    SolicitudPrediccionIndividual obtenerPorId(Long id);


}
