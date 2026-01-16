package org.hackaton.oracle.churninsight.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "tbl_usuario_rol",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_usuario_rol",
                        columnNames = {"id_usuario", "id_rol"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_rol", nullable = false)
    private Long idRol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private UsuarioAutenticacion usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    public static UsuarioRol crear(Long idUsuario, Long idRol) {
        UsuarioRol ur = new UsuarioRol();
        ur.idUsuario = idUsuario;
        ur.idRol = idRol;
        return ur;
    }

}
