package org.hackaton.oracle.churninsight.web.controller;

import jakarta.validation.Valid;
import org.hackaton.oracle.churninsight.infra.security.service.AuthService;
import org.hackaton.oracle.churninsight.infra.service.UsuarioService;
import org.hackaton.oracle.churninsight.web.dto.auth.DatosRegistroWrapper;
import org.hackaton.oracle.churninsight.web.dto.auth.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;
    UsuarioService service;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.service = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(
            @RequestBody @Valid DatosRegistroWrapper datos,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var usuario = service.registrar(datos.usuario());
        authService.registrarCredenciales(usuario, datos.credenciales().password());
        var uri = uriComponentsBuilder.path("/usuario/{id}")
                .buildAndExpand(usuario.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest login) {
        var usuarioAutenticado = authService.autenticar(login);
        return ResponseEntity.ok(usuarioAutenticado);
    }
}
