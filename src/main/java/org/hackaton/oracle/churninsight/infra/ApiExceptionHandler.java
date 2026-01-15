package org.hackaton.oracle.churninsight.infra;

import org.hackaton.oracle.churninsight.infra.Exception.UsuarioNotFoundException;
import org.hackaton.oracle.churninsight.web.dto.error.ApiErrorResponseDTO;
import org.hackaton.oracle.churninsight.web.dto.error.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(ApiExceptionHandler.class);


    //Metodo para manejar  error 404 / usuario o prediccion no encontrada
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiErrorResponseDTO>handleError404(RuntimeException ex){
        logger.warn("Recurso No encontrado: {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Recurso no Encontrado",
                        ex.getMessage()
                ));

    }

    //Metodo para Manejar Error 400(por validacion de campos de los dto)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDTO>handleErrorValidationDto(MethodArgumentNotValidException ex){
        var errores=ex.getFieldErrors()
                        .stream()
                                .map(ValidationErrorDTO::new)
                                        .toList();

        logger.warn("Error de validacion: {}",errores);

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Error de validacion",
                        "uno o mas campos invalidos",
                        errores
                ));
    }

    //Metodo para manejar Error 400 Por argumentos invalidos en los Services(Logica)
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiErrorResponseDTO>handleErrorIllegalArgument(IllegalArgumentException ex){
        logger.warn("Argumento invalido : {}",ex.getMessage());

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Peticion invalida",
                        ex.getMessage()
                        )
                );

    }

    //Metodo para manejar Error 500 errores internos no controlados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO>handleError500(Exception ex){
        logger.warn("Error inesperado : {}",ex.getMessage());

        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor ,Contactese con el Administrador",
                        ex.getMessage()
                ));
    }


    //Metodo para manejar error 415 Envio incorrecto del formato del cuerpo de la peticion
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponseDTO>handleError415(HttpMediaTypeNotSupportedException ex){
        logger.warn("formato invalido de peticion :{}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiErrorResponseDTO(
                        LocalDateTime.now(),
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                        "Tipo de contenido no soportado",
                        "Debe usar ´application/json´ en el encabezado Content-Type"
                ));
    }


}
