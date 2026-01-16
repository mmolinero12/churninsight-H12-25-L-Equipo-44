package org.hackaton.oracle.churninsight.infra.service;

import org.hackaton.oracle.churninsight.web.dto.usuario.DatosActualizacionUsuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosDetalleUsuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosListaUsuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosRegistroUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    //se cambio el tipo de respuesta que se manejaba al realizar una actualizacion
    DatosDetalleUsuario actualizar(DatosActualizacionUsuario datos, Long id);

    DatosDetalleUsuario registrar(DatosRegistroUsuario datos);

    DatosDetalleUsuario detallar(Long id);

    Page<DatosListaUsuario> listar(Pageable pageable);
}
