package org.hackaton.oracle.churninsight.domain.ml.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tbl_archivo_resultado_batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class ArchivoResultadoBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       Relación con solicitud batch
       ========================= */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitud_batch", nullable = false, unique = true)
    private SolicitudPrediccionBatch solicitudBatch;

    /* =========================
       Archivo CSV resultado
       ========================= */

    @Column(name = "resultado_csv", nullable = false, columnDefinition = "bytea")
    private byte[] resultadoCsv;

    @Column(name = "resultado_csv_name", nullable = false, length = 255)
    private String resultadoCsvName;

    /* =========================
       Auditoría
       ========================= */

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion;
}
