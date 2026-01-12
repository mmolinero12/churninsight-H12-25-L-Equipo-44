package org.hackaton.oracle.churninsight.domain.ml.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;


@Entity
@Table(name = "tbl_solicitud_prediccion_individual")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class SolicitudPrediccionIndividual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       Relación con Modelo
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;

    /* =========================
       Datos del solicitante
       ========================= */
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /* =========================
       Estado de la solicitud
       ========================= */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPrediccion estado;

    /* =========================
       Request (JSONB)
       ========================= */
    /**
     * JSON enviado en predicciones individuales.
     * En batch suele ser NULL.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_json", columnDefinition = "jsonb")
    private JsonNode requestJson;

    /* =========================
       Auditoría
       ========================= */
    @Column(name = "fecha_solicitud", nullable = false)
    private OffsetDateTime fechaSolicitud;

    @Column(name = "fecha_respuesta")
    private OffsetDateTime fechaRespuesta;

    /* =========================
       Error
       ========================= */
    @Column(name = "mensaje_error", columnDefinition = "TEXT")
    private String mensajeError;


    /* =========================
       Métodos
       ========================= */

    public void marcarComoProcesando() {
        this.estado = EstadoPrediccion.PROCESANDO;
    }

    public void marcarComoCompletada() {
        this.estado = EstadoPrediccion.COMPLETADA;
    }

    public void marcarComoError(String mensaje) {
        this.estado = EstadoPrediccion.ERROR;
        this.mensajeError = mensaje;
    }

    public void marcarFechaRespuesta() {
        this.fechaRespuesta = OffsetDateTime.now();
    }


}
