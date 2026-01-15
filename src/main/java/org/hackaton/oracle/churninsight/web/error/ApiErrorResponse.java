package org.hackaton.oracle.churninsight.web.error;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        LocalDateTime timeStamp,
        int status,
        String error,
        String mensaje,
        List<ValidationError> validationErrors
)
 {
     public ApiErrorResponse(LocalDateTime timeStamp, int status, String error, String mensaje) {
         this(timeStamp,status,error,mensaje,null);
     }

}
