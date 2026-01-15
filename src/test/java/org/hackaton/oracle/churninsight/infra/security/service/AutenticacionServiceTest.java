package org.hackaton.oracle.churninsight.infra.security.service;

import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioAutenticacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest {

    @Mock
    private UsuarioAutenticacionRepository usuarioRepository;

    @InjectMocks
    private AutenticacionService autenticacionService;

    @Test
    void loadUserByUsername_usuarioExiste_retornaUserDetails() {
        String username = "testuser";
        UsuarioAutenticacion usuario = mock(UsuarioAutenticacion.class);

        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.of(usuario));

        UserDetails resultado =
                autenticacionService.loadUserByUsername(username);

        assertNotNull(resultado);
        assertEquals(usuario, resultado);
        verify(usuarioRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaExcepcion() {
        String username = "no-existe";

        when(usuarioRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> autenticacionService.loadUserByUsername(username)
        );
        assertEquals(
                "Usuario no encontrado: " + username,
                exception.getMessage()
        );

        verify(usuarioRepository).findByUsername(username);
    }
}
