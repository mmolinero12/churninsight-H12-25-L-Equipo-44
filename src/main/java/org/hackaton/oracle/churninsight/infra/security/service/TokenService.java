package org.hackaton.oracle.churninsight.infra.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.hackaton.oracle.churninsight.domain.auth.entity.UsuarioAutenticacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generarToken(UsuarioAutenticacion usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create().withIssuer("Hackaton").withSubject(usuario.getUsername())
                    .withExpiresAt(fechaLimite()).sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Error generando Token, prueba de nuevo.");
        }
    }

    private Instant fechaLimite() {
        return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-06:00"));
    }

    public String getSubject(String TokenJWT) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            return JWT.require(algorithm).withIssuer("Hackaton")
                    .build()
                    .verify(TokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("TokenJWT inválido: " + exception.getMessage());
        }
    }

}


