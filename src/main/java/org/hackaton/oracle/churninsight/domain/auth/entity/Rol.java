package org.hackaton.oracle.churninsight.domain.auth.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_rol", schema = "public")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_rol", nullable = false, length = 50, unique = true)
    private String nombreRol; // Valores: "USER", "ANALYST", "ADMIN"

}
