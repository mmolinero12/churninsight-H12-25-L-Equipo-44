package org.hackaton.oracle.churninsight.infra.mapper;

import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosActualizacionDireccion;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosRegistroDireccion;
import org.springframework.stereotype.Component;

@Component
public class DireccionMapper {


    //Metodo para mapear un registro de una Direccion
    public Direccion registerDtoToEntity(DatosRegistroDireccion dto) {
        if (dto == null) return null;

        return new Direccion(
                dto.calleNumero(),
                dto.colonia(),
                dto.alcaldiaMunicipio(),
                dto.ciudad(),
                dto.estado(),
                dto.codigoPostal()
        );
    }

    //Metodo para mapear una actualizacion de una Direccion
    public Direccion ActualizacionDtoToEntity(DatosActualizacionDireccion dto) {
        if (dto == null) return null;

        return new Direccion(
                dto.calleNumero(),
                dto.colonia(),
                dto.alcaldiaMunicipio(),
                dto.ciudad(),
                dto.estado(),
                dto.codigoPostal()
        );
    }



}
