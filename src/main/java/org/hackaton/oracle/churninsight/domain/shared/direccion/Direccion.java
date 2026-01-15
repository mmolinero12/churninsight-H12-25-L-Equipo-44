package org.hackaton.oracle.churninsight.domain.shared.direccion;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter           // lombok
@ToString               // lombok
@NoArgsConstructor   // lombok // lombok
@Embeddable             // jakarta
public class Direccion {

    @Column(name = "calle_numero")      // @Column de jakarta
    private String calleNumero;
    private String colonia;
    @Column(name = "alcaldia_municipio")
    private String alcaldiaMunicipio;
    private String ciudad;
    private String estado;
    @Column(name = "codigo_postal")
    private String codigoPostal;

    public Direccion(
            String calleNumero,
            String colonia,
            String alcaldiaMunicipio,
            String ciudad,
            String estado,
            String codigoPostal
    ) {
        this.calleNumero = calleNumero;
        this.colonia = colonia;
        this.alcaldiaMunicipio = alcaldiaMunicipio;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
    }


    /* -- Metodo de Negocio (C) */
    public void actualizarDireccion(Direccion nuevaDireccion) {
        if (nuevaDireccion.getCalleNumero() != null) {
            this.calleNumero = nuevaDireccion.getCalleNumero();
        }
        if (nuevaDireccion.getColonia() != null) {
            this.colonia = nuevaDireccion.getColonia();
        }
        if (nuevaDireccion.getAlcaldiaMunicipio() != null) {
            this.alcaldiaMunicipio = nuevaDireccion.getAlcaldiaMunicipio();
        }
        if (nuevaDireccion.getCiudad() != null) {
            this.ciudad = nuevaDireccion.getCiudad();
        }
        if (nuevaDireccion.getEstado() != null) {
            this.estado = nuevaDireccion.getEstado();
        }
        if (nuevaDireccion.getCodigoPostal()!= null) {
            this.codigoPostal = nuevaDireccion.getCodigoPostal();
        }
    }
}
