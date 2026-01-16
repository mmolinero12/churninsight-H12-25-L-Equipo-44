package org.hackaton.oracle.churninsight.domain.usuario.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hackaton.oracle.churninsight.domain.shared.direccion.Direccion;
import org.hackaton.oracle.churninsight.domain.shared.enums.sexo.Sexo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Usuario")  // nombre de la entidad para consultas JPQL.
@Table(name = "tbl_usuario", schema = "public")
@Getter   // @Setter se elimina con el objetivo de NO perder el control del modelo
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // Indica al programa la condición para que dos objetos sean iguales (lombok)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // evita inclusiones accidentales si mañana alguien agrega un campo
    private Long id;

    private String nombre;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)        // jakarta
    @Column(name = "sexo", length = 1)  // Esto define la longitud máxima en la columna cuando JPA genera el esquema (DDL).
    private Sexo sexo;                  // Aunque tu columna ya es CHAR(1) en la base de datos, es recomendable mantenerlo
    // para que Hibernate sepa que debe usar un tamaño de 1 carácter al mapear o
    // validar datos.

    private String curp;

    private String telefono;

    private String email;



    @Embedded                           // jakarta
    @AttributeOverrides({               // jakarta
            @AttributeOverride(
                    // Atributo de Java en la clase Direccion
                    name = "calleNumero",
                    // Columna SQL de la tabla tbl_usuario
                    column = @Column(name = "calle_numero")
            ),
            @AttributeOverride(
                    // Atributo de Java en la clase Direccion
                    name = "codigoPostal",
                    // Columna SQL de la tabla tbl_usuario
                    column = @Column(name = "codigo_postal")
            )
    })
    private Direccion direccion;


    @Column(name = "fecha_ingreso",nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;


    /* --Constructor del Dominio (C)*/
    public Usuario(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            LocalDate fechaNacimiento,
            Sexo sexo,
            String curp,
            String telefono,
            String email,
            Direccion direccion
    ) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.curp = curp;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaIngreso = LocalDateTime.now();
    }


    /* --Metodos de Negocio-- (C) */
    public void actualizarTelefono(String telefono) {
        if (telefono != null) {
            this.telefono = telefono;
        }
    }

    public void actualizarDireccion(Direccion nuevaDireccion) {
        if (nuevaDireccion != null) {
            this.direccion.actualizarDireccion(nuevaDireccion);
        }
    }

}
