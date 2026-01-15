package org.hackaton.oracle.churninsight.infra.mapper;

import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.usuario.entity.Usuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosDetalleUsuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosListaUsuario;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosRegistroUsuario;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UsuarioMapper {

    //Metodo para mapear un registro de un Usuario
    public Usuario registroDtoToEntity(DatosRegistroUsuario datos, Direccion direccion) {
        if (datos == null) return null;

        return new Usuario(
                datos.nombre(),
                datos.apellidoPaterno(),
                datos.apellidoMaterno(),
                datos.fechaNacimiento(),
                datos.sexo(),
                datos.curp(),
                datos.telefono(),
                datos.email(),
                direccion
        );
    }

    //Metodo para mapear una entidad a un datosListaDto
    public DatosListaUsuario entityToListaDto(Usuario usuario) {
        if (usuario == null) return null;

        return new DatosListaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDireccion()
        );
    }

    //Metodo para mapear una lista usuariosDTO
    public List<DatosListaUsuario> entityListToListaDto(List<Usuario>usuarios) {
        return usuarios.stream()
                .map(this::entityToListaDto)
                .toList();
    }



    //Metodo para mapear a un usuario detallado
    public DatosDetalleUsuario entityToDetalleDto(Usuario usuario) {
        if (usuario == null) return null;

        return new DatosDetalleUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno(),
                usuario.getFechaNacimiento(),
                usuario.getSexo(),
                usuario.getCurp(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getFechaIngreso()
        );
    }


}


