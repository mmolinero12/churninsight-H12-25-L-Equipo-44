package org.hackaton.oracle.churninsight.domain.ml.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tbl_resultado_prediccion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class ResultadoPrediccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       Relación con solicitudes
       ========================= */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_individual")
    private SolicitudPrediccionIndividual solicitudIndividual;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_batch")
    private SolicitudPrediccionBatch solicitudBatch;

    /* =========================
       Resultado lógico
       ========================= */

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resultado_json", columnDefinition = "jsonb", nullable = false)
    private JsonNode resultadoJson;

    /* =========================
       Auditoría
       ========================= */

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion;


    // -------------------------
    // Fábricas explícitas
    // -------------------------

    public static ResultadoPrediccion paraIndividual(
            SolicitudPrediccionIndividual solicitud,
            JsonNode resultadoJson
    ) {
        ResultadoPrediccion r = new ResultadoPrediccion();
        r.solicitudIndividual = solicitud;
        r.resultadoJson = resultadoJson;
        r.fechaCreacion = OffsetDateTime.now();
        return r;

    }



}
