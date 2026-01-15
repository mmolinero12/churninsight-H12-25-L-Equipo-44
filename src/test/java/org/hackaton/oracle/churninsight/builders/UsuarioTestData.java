package org.hackaton.oracle.churninsight.builders;

import org.hackaton.oracle.churninsight.domain.shared.enums.sexo.Sexo;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosActualizacionUsuario;
import org.hackaton.oracle.churninsight.web.dto.direccion.DatosRegistroDireccion;
import org.hackaton.oracle.churninsight.web.dto.usuario.DatosRegistroUsuario;

import java.time.LocalDate;

public class UsuarioTestData {

    static DatosRegistroDireccion direccionValida() {
        return new DatosRegistroDireccion(
                "Av. Reforma 123",
                "Centro",
                "Cuauhtémoc",
                "Ciudad de México",
                "CDMX",
                "01000"
        );
    }

    public static DatosRegistroUsuario registroValido() {
        return new DatosRegistroUsuario(
                "Juan",
                "Pérez",
                "Gómez",
                LocalDate.of(1995, 5, 20),
                Sexo.M,
                "ABCD950520HMNLRR09",
                "juan@mail.com",
                "5512345678",
                direccionValida(),
                LocalDate.now()
        );
    }

    public static DatosActualizacionUsuario actualizacionValida(Long id) {
        return new DatosActualizacionUsuario(
                id,
                "5598765432",
                null
        );
    }

}