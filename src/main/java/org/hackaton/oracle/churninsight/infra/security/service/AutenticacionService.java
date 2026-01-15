package org.hackaton.oracle.churninsight.infra.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.hackaton.oracle.churninsight.domain.auth.repository.UsuarioAutenticacionRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor()
public class AutenticacionService implements UserDetailsService {
    private final UsuarioAutenticacionRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + username)
                );
    }
}