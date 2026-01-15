package org.hackaton.oracle.churninsight.web.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

//permite que se serializen los campos opcionales solo si tienen datos relevantes
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponseDTO(
        LocalDateTime timeStamp,
        int status,
        String error,
        String mensaje,
        List<ValidationErrorDTO>validationErrors
) {

    public ApiErrorResponseDTO(LocalDateTime timeStamp, int status, String error, String mensaje) {
     this(timeStamp,status,error,mensaje,null);
    }
}
