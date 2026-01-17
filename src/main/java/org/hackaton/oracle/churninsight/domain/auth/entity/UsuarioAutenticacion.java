package org.hackaton.oracle.churninsight.domain.auth.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_usuario_autenticacion", schema = "public")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class UsuarioAutenticacion implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "password_temporal", nullable = false)
    private Boolean passwordTemporal = false;

    @Column(name = "password_expiration")
    private LocalDateTime passwordExpiration;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<UsuarioRol> usuarioRoles;


    public static UsuarioAutenticacion crear(
            String username,
            String password,
            Long usuarioId
    ) {
        UsuarioAutenticacion u = new UsuarioAutenticacion();
        u.username = username;
        u.password = password;
        u.usuarioId = usuarioId;
        u.passwordTemporal = false;
        u.enabled = true;
        u.locked = false;
        u.passwordExpiration = null;
        return u;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuarioRoles == null) return List.of();

        return usuarioRoles.stream()
                .map(UsuarioRol::getRol)
                .map(Rol::getNombreRol)
                .map(SimpleGrantedAuthority::new) // quita el "ROLE_" extra
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return passwordExpiration == null || passwordExpiration.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
