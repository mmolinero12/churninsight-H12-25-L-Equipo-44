package org.hackaton.oracle.churninsight.infra.service;

import lombok.RequiredArgsConstructor;
import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.usuario.entity.Usuario;
import org.hackaton.oracle.churninsight.domain.usuario.repository.UsuarioRepository;
import org.hackaton.oracle.churninsight.infra.exception.UsuarioNotFoundException;
import org.hackaton.oracle.churninsight.infra.mapper.DireccionMapper;
import org.hackaton.oracle.churninsight.infra.mapper.UsuarioMapper;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosActualizacionDireccion;
import org.hackaton.oracle.churninsight.web.dto.usuario.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final DireccionMapper direccionMapper;

    // 👇 AQUÍ VA EL TRANSACTION MANAGER CORRECTO
    @Override
    @Transactional(transactionManager = "usuariosTransactionManager")
    public DatosDetalleUsuario registrar(DatosRegistroUsuario datos) {

        Direccion direccion = direccionMapper.registerDtoToEntity(datos.direccion());
        Usuario usuario = usuarioMapper.registroDtoToEntity(datos, direccion);

        usuarioRepository.save(usuario);

        return usuarioMapper.entityToDetalleDto(usuario);
    }

    @Override
    @Transactional(transactionManager = "usuariosTransactionManager")
    public DatosDetalleUsuario actualizar(DatosActualizacionUsuario datos, Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException(
                                "Usuario con id " + id + " no existe"
                        )
                );

        // --- Reglas de dominio ---
        usuario.actualizarTelefono(datos.telefono());

        if (datos.direccion() != null) {
            usuario.actualizarDireccion(
                    direccionMapper.ActualizacionDtoToEntity(datos.direccion())
            );
        }

        return usuarioMapper.entityToDetalleDto(usuario);
    }

    /*
    Uso de .getReferenceById(). Aquí ya se acceden campos, por lo tanto:
        Se ejecuta el SELECT
        NO hay LazyInitializationException
        Porque estás dentro de @Transactional
     */

    @Override
    @Transactional(
            transactionManager = "usuariosTransactionManager",
            readOnly = true
    )
    public DatosDetalleUsuario detallar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException(
                                "Usuario con id " + id + " no existe"
                        )
                );

        return usuarioMapper.entityToDetalleDto(usuario);
    }


    @Override
    @Transactional(
            transactionManager = "usuariosTransactionManager",
            readOnly = true
    )
    public Page<DatosListaUsuario> listar(Pageable pageable) {

        return usuarioRepository.findAll(pageable)
                .map(usuarioMapper::entityToListaDto);
    }



}
