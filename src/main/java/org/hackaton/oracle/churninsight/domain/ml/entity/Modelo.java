package org.hackaton.oracle.churninsight.domain.ml.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hackaton.oracle.churninsight.domain.shared.enums.ml.EstadoModelo;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "tbl_modelo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_modelo_nombre_version",
                        columnNames = {"nombre", "version"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String version;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoModelo estado;

    @Column(name = "soporta_individual", nullable = false)
    private Boolean soportaIndividual;

    @Column(name = "soporta_batch", nullable = false)
    private Boolean soportaBatch;

    @Column(name = "endpoint_individual", columnDefinition = "TEXT")
    private String endpointIndividual;

    @Column(name = "endpoint_batch", columnDefinition = "TEXT")
    private String endpointBatch;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_activacion")
    private OffsetDateTime fechaActivacion;

    @Column(name = "fecha_desactivacion")
    private OffsetDateTime fechaDesactivacion;


    public void cambiarEstado(EstadoModelo nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean soportaPrediccionIndividual() {
        return Boolean.TRUE.equals(this.soportaIndividual);
    }

    public boolean soportaPrediccionBatch() {
        return Boolean.TRUE.equals(this.soportaBatch);
    }



}
