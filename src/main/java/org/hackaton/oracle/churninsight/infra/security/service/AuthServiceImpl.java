package org.hackaton.oracle.churninsight.infra.security.service;

import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.auth.entity.Rol;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioRol;
import org.hackaton.oracle.churninsight.domain.auth.repository.RolRepository;
import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioAutenticacionRepository;
import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioRolRepository;
import org.hackaton.oracle.churninsight.web.dto.auth.DatosTokenJWT;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosDetalleUsuario;
import org.hackaton.oracle.churninsight.web.dto.auth.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioAutenticacionRepository authRepo;
    private final UsuarioRolRepository usuarioRolRepo;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager manager;

    @Override
    @Transactional
    public void registrarCredenciales(DatosDetalleUsuario usuario, String password) {

        UsuarioAutenticacion auth = UsuarioAutenticacion.crear(
                usuario.email(),
                passwordEncoder.encode(password),
                usuario.id()
        );

        auth.setPasswordTemporal(false);
        auth.setPasswordExpiration(LocalDateTime.now().plusDays(90));
        auth.setEnabled(true);
        auth.setLocked(false);

        authRepo.save(auth);

        Rol rolUser = rolRepository.findByNombreRol("ROLE_ANALYST")
                .orElseThrow(() ->
                        new IllegalStateException("Rol ROLE_ANALYST no existe")
                );


        UsuarioRol usuarioRol = UsuarioRol.crear(
                auth.getId(),
                rolUser.getId()
        );

        usuarioRolRepo.save(usuarioRol);
    }

    @Override
    public DatosTokenJWT autenticar(LoginRequest login) {
        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.password()
                );

        var authentication = manager.authenticate(authenticationToken);

        var token = tokenService.generarToken(
                (UsuarioAutenticacion) authentication.getPrincipal()
        );

        // Agregando rol

        String rol = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() ->
                        new IllegalStateException("Usuario autenticado sin rol asignado")
                );

        return new DatosTokenJWT(token, rol);
    }
}
