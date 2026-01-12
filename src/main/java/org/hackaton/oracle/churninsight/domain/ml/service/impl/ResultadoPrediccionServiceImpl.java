package org.hackaton.oracle.churninsight.domain.ml.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.ml.entity.ResultadoPrediccion;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionBatch;
import org.hackaton.oracle.churninsight.domain.ml.entity.SolicitudPrediccionIndividual;
import org.hackaton.oracle.churninsight.domain.ml.repository.ResultadoPrediccionRepository;
import org.hackaton.oracle.churninsight.domain.ml.repository.SolicitudPrediccionBatchRepository;
import org.hackaton.oracle.churninsight.domain.ml.repository.SolicitudPrediccionIndividualRepository;
import org.hackaton.oracle.churninsight.domain.ml.service.ResultadoPrediccionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "mlOperationsTransactionManager")
public class ResultadoPrediccionServiceImpl
        implements ResultadoPrediccionService{

    private final ResultadoPrediccionRepository resultadoRepository;
    private final SolicitudPrediccionIndividualRepository individualRepository;
    private final SolicitudPrediccionBatchRepository batchRepository;

    @Override
    public ResultadoPrediccion guardarResultadoIndividual(
            Long idSolicitudIndividual,
            JsonNode resultadoJson
    ) {

        SolicitudPrediccionIndividual solicitud =
                individualRepository.findById(idSolicitudIndividual)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Solicitud individual no encontrada"));

        ResultadoPrediccion resultado =
                ResultadoPrediccion.builder()
                        .solicitudIndividual(solicitud)
                        .resultadoJson(resultadoJson)
                        .fechaCreacion(OffsetDateTime.now())
                        .build();

        return resultadoRepository.save(resultado);
    }

    @Override
    public ResultadoPrediccion guardarResultadoBatch(
            Long idSolicitudBatch,
            JsonNode resultadoJson
    ) {

        SolicitudPrediccionBatch solicitud =
                batchRepository.findById(idSolicitudBatch)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Solicitud batch no encontrada"));

        ResultadoPrediccion resultado =
                ResultadoPrediccion.builder()
                        .solicitudBatch(solicitud)
                        .resultadoJson(resultadoJson)
                        .fechaCreacion(OffsetDateTime.now())
                        .build();

        return resultadoRepository.save(resultado);
    }


    @Override
    public Optional<ResultadoPrediccion> obtenerPorSolicitudIndividual(
            Long idSolicitud
    ) {

        SolicitudPrediccionIndividual solicitud =
                individualRepository.findById(idSolicitud)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Solicitud individual no encontrada con id " + idSolicitud
                                ));

        return resultadoRepository.findBySolicitudIndividual(solicitud);
    }

    @Override
    public Page<ResultadoPrediccion> listar(Pageable pageable) {
        return resultadoRepository.findAll(pageable);
    }

    @Override
    public ResultadoPrediccion obtenerPorId(Long id) {
        return resultadoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "ResultadoPrediccion no encontrado con id " + id
                        )
                );
    }

}
