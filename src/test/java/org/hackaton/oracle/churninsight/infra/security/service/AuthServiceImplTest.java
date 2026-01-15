package org.hackaton.oracle.churninsight.infra.security.service;

import org.hackaton.oracle.churninsight.domain.auth.entity.Rol;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioRol;
import org.hackaton.oracle.churninsight.domain.auth.repository.RolRepository;
import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioAutenticacionRepository;
import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioRolRepository;
import org.hackaton.oracle.churninsight.web.dto.auth.DatosTokenJWT;
import org.hackaton.oracle.churninsight.web.dto.auth.LoginRequest;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosDetalleUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioAutenticacionRepository authRepo;

    @Mock
    private UsuarioRolRepository usuarioRolRepo;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager manager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registrarCredenciales_debeCrearAuthYAsignarRol() {
        DatosDetalleUsuario usuario = mock(DatosDetalleUsuario.class);
        when(usuario.email()).thenReturn("test@mail.com");
        when(usuario.id()).thenReturn(1L);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        Rol rol = mock(Rol.class);
        when(rol.getId()).thenReturn(10L);

        when(rolRepository.findByNombreRol("ROLE_ANALYST"))
                .thenReturn(Optional.of(rol));

        authService.registrarCredenciales(usuario, "123456");

        verify(authRepo).save(any(UsuarioAutenticacion.class));
        verify(usuarioRolRepo).save(any(UsuarioRol.class));
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void registrarCredenciales_sinRol_lanzaExcepcion() {
        DatosDetalleUsuario usuario = mock(DatosDetalleUsuario.class);

        when(rolRepository.findByNombreRol("ROLE_ANALYST"))
                .thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> authService.registrarCredenciales(usuario, "123456")
        );

        assertEquals(
                "Rol ROLE_ANALYST no existe",
                exception.getMessage()
        );

        verify(authRepo).save(any(UsuarioAutenticacion.class));
        verify(usuarioRolRepo, never()).save(any());
    }

    @Test
    void autenticar_debeGenerarTokenJWT() {
        LoginRequest login = mock(LoginRequest.class);
        when(login.email()).thenReturn("test@mail.com");
        when(login.password()).thenReturn("password");

        UsuarioAutenticacion principal = mock(UsuarioAutenticacion.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(manager.authenticate(any()))
                .thenReturn(authentication);

        when(tokenService.generarToken(principal))
                .thenReturn("jwt-token");

        DatosTokenJWT resultado = authService.autenticar(login);

        assertNotNull(resultado);
        assertEquals("jwt-token", resultado.token());

        verify(manager).authenticate(any());
        verify(tokenService).generarToken(principal);
    }
}
