package org.hackaton.oracle.churninsight.infra.service;

import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.usuario.entity.Usuario;
import org.hackaton.oracle.churninsight.domain.usuario.repository.UsuarioRepository;
import org.hackaton.oracle.churninsight.infra.exception.UsuarioNotFoundException;
import org.hackaton.oracle.churninsight.infra.mapper.DireccionMapper;
import org.hackaton.oracle.churninsight.infra.mapper.UsuarioMapper;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosActualizacionDireccion;
import org.hackaton.oracle.churninsight.web.dto.usuario.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void registrar_debeGuardarUsuarioYRetornarDetalle() {
        DatosRegistroUsuario datosRegistro = mock(DatosRegistroUsuario.class);
        Direccion direccion = mock(Direccion.class);
        Usuario usuario = mock(Usuario.class);
        DatosDetalleUsuario detalle = mock(DatosDetalleUsuario.class);

        when(direccionMapper.registerDtoToEntity(datosRegistro.direccion()))
                .thenReturn(direccion);
        when(usuarioMapper.registroDtoToEntity(datosRegistro, direccion))
                .thenReturn(usuario);
        when(usuarioMapper.entityToDetalleDto(usuario))
                .thenReturn(detalle);

        DatosDetalleUsuario resultado = usuarioService.registrar(datosRegistro);

        verify(usuarioRepository).save(usuario);
        assertEquals(detalle, resultado);
    }

    @Test
    void actualizar_debeActualizarTelefonoYDireccion() {
        Long id = 1L;
        Usuario usuario = mock(Usuario.class);
        DatosActualizacionUsuario datos = mock(DatosActualizacionUsuario.class);
        DatosActualizacionDireccion datosDireccion = mock(DatosActualizacionDireccion.class);
        Direccion direccion = mock(Direccion.class);
        DatosDetalleUsuario detalle = mock(DatosDetalleUsuario.class);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(datos.telefono()).thenReturn("999999999");
        when(datos.direccion()).thenReturn(datosDireccion);
        when(direccionMapper.ActualizacionDtoToEntity(datosDireccion))
                .thenReturn(direccion);
        when(usuarioMapper.entityToDetalleDto(usuario))
                .thenReturn(detalle);

        DatosDetalleUsuario resultado = usuarioService.actualizar(datos, id);

        verify(usuario).actualizarTelefono("999999999");
        verify(usuario).actualizarDireccion(direccion);
        assertEquals(detalle, resultado);
    }

    @Test
    void actualizar_sinDireccion_noDebeActualizarDireccion() {
        Long id = 1L;
        Usuario usuario = mock(Usuario.class);
        DatosActualizacionUsuario datos = mock(DatosActualizacionUsuario.class);
        DatosDetalleUsuario detalle = mock(DatosDetalleUsuario.class);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(datos.telefono()).thenReturn("888888888");
        when(datos.direccion()).thenReturn(null);
        when(usuarioMapper.entityToDetalleDto(usuario))
                .thenReturn(detalle);

        usuarioService.actualizar(datos, id);

        verify(usuario).actualizarTelefono("888888888");
        verify(usuario, never()).actualizarDireccion(any());
    }

    @Test
    void actualizar_usuarioNoExiste_lanzaExcepcion() {
        Long id = 99L;
        DatosActualizacionUsuario datos = mock(DatosActualizacionUsuario.class);

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> usuarioService.actualizar(datos, id)
        );
    }

    @Test
    void detallar_usuarioExiste_retornaDetalle() {
        Long id = 1L;
        Usuario usuario = mock(Usuario.class);
        DatosDetalleUsuario detalle = mock(DatosDetalleUsuario.class);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.entityToDetalleDto(usuario))
                .thenReturn(detalle);

        DatosDetalleUsuario resultado = usuarioService.detallar(id);

        assertEquals(detalle, resultado);
    }

    @Test
    void detallar_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                UsuarioNotFoundException.class,
                () -> usuarioService.detallar(1L)
        );
    }

    @Test
    void listar_debeRetornarPaginaDeUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);
        Usuario usuario = mock(Usuario.class);
        DatosListaUsuario dto = mock(DatosListaUsuario.class);

        Page<Usuario> page = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageable)).thenReturn(page);
        when(usuarioMapper.entityToListaDto(usuario))
                .thenReturn(dto);

        Page<DatosListaUsuario> resultado = usuarioService.listar(pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals(dto, resultado.getContent().get(0));
    }
}
