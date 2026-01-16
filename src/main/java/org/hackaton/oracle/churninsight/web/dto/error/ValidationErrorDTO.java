package org.hackaton.oracle.churninsight.web.dto.error;

import org.springframework.validation.FieldError;

//Dto para manejar errores de validacion de campos (@Valid)
public record ValidationErrorDTO(
        String campo,
        String mensaje
) {
    public ValidationErrorDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }

}
