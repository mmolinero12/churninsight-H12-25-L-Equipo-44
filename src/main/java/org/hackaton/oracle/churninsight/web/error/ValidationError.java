package org.hackaton.oracle.churninsight.web.error;


import org.springframework.validation.FieldError;

public record ValidationError(
        String campo,
        String mensaje
) {

    public ValidationError(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}
