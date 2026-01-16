package org.hackaton.oracle.churninsight.infra.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private TokenService tokenService;

    private static final String SECRET = "test-secret-123";

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();

        // Inyectar el secret manualmente (@Value no funciona en unit tests)
        Field secretField = TokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, SECRET);
    }

    @Test
    void generarToken_debeGenerarTokenValido() {
        UsuarioAutenticacion usuario = mock(UsuarioAutenticacion.class);
        when(usuario.getUsername()).thenReturn("testuser");

        String token = tokenService.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());

        String subject = JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer("Hackaton")
                .build()
                .verify(token)
                .getSubject();

        assertEquals("testuser", subject);
    }

    @Test
    void getSubject_debeRetornarSubject() {
        UsuarioAutenticacion usuario = mock(UsuarioAutenticacion.class);
        when(usuario.getUsername()).thenReturn("subject-user");

        String token = tokenService.generarToken(usuario);

        String subject = tokenService.getSubject(token);

        assertEquals("subject-user", subject);
    }

    @Test
    void getSubject_tokenInvalido_lanzaExcepcion() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> tokenService.getSubject("token-invalido")
        );

        assertTrue(exception.getMessage().startsWith("TokenJWT inválido"));
    }
}
