package org.hackaton.oracle.churninsight.domain.ml.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoPrediccion;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tbl_solicitud_prediccion_batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class SolicitudPrediccionBatch {

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
       Input CSV (BATCH)
       ========================= */
    @Column(name = "input_csv", nullable = false, columnDefinition = "bytea")
    private byte[] inputCsv;

    @Column(name = "input_csv_name", nullable = false, length = 255)
    private String inputCsvName;

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
