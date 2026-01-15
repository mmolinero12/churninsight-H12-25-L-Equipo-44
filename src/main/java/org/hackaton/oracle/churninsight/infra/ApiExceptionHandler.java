package org.hackaton.oracle.churninsight.infra;

import io.jsonwebtoken.JwtException;
import org.hackaton.oracle.churninsight.infra.exception.UsuarioNotFoundException;
import org.hackaton.oracle.churninsight.web.error.ApiErrorResponse;
import org.hackaton.oracle.churninsight.web.error.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import javax.security.sasl.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger= LoggerFactory.getLogger(ApiExceptionHandler.class);


    //Metodo para manejar  error 404 / usuario o prediccion no encontrada
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleError404(RuntimeException ex){
        logger.warn("Recurso No encontrado: {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "Recurso no Encontrado",
                        ex.getMessage()
                ));

    }

    //Metodo para Manejar Error 400(por validacion de campos de los dto)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorValidationDto(MethodArgumentNotValidException ex){
        var errores=ex.getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .toList();

        logger.warn("Error de validacion: {}",errores);

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Error de validacion",
                        "uno o mas campos invalidos",
                        errores
                ));
    }

    //Metodo para manejar Error 400 Por argumentos invalidos en los Services(Logica)
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorIllegalArgument(IllegalArgumentException ex){
        logger.warn("Argumento invalido : {}",ex.getMessage());

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Peticion invalida",
                                ex.getMessage()
                        )
                );

    }

    //Metodo para Manejar error 401 {UnauThorized} Cualquier excepción de autenticación de Spring Security
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorAuthentication(AuthenticationException ex){
        logger.warn("No Autenticado : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "Credenciales inválidas",
                        "Usuario o contraseña incorrectos"
                ));
    }

    //Metodo para manejar Error 401 por Token invalido o Expirado
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorJWT(JwtException ex){

        logger.warn("Jwt Invalido : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "Token Invalido",
                        "El Token es Invalido o a Expirado"
                ));
    }

    //Metodo para manejar Error 403 {Forbbiden} Rol incorrecto o usuario autenticado pero sin permisos
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorAccessDenied(AccessDeniedException ex){
        logger.warn("Acceso Denegado : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        "Acceso Denegado",
                        "No Tiene Permisos para Acceder a este recurso"
                ));
    }

    //Metodo pára manejar Error 400(Bad Request) Jso Invalido , CsV mal parseado,error en deserializacion
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse>handleErrorJsonMalFormad(HttpMessageNotReadableException ex){
        logger.warn("Json Mal Formado : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.FORBIDDEN.value(),
                        "Cuerpo de la Peticion Invalido",
                        "Json mal Formado o Tipos Incorrectos"
                ));
    }

    //Metodo para manejar error 409 {Conflict} Usuario existente,Modelo Duplicado,Prediccion ya registrada
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse>hanledError409(DataIntegrityViolationException ex){
        logger.warn("Conflicto de datos : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        "Conflicto de Datos",
                        "El recurso ya existe o viola una Restriccion"
                ));
    }

    //Metodo para manejar error 415 Envio incorrecto del formato del cuerpo de la peticion
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse>handleError415(HttpMediaTypeNotSupportedException ex){
        logger.warn("formato invalido de peticion :{}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                        "Tipo de contenido no soportado",
                        "Debe usar ´application/json´ en el encabezado Content-Type"
                ));
    }

    //Metodo para manejar Error 500 errores internos no controlados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse>handleError500(Exception ex){
        logger.warn("Error inesperado : {}",ex.getMessage());

        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor ,Contactese con el Administrador",
                        ex.getMessage()
                ));
    }


    //Metodo para manejar Error 504 {Time Out GateWay}Timeout al consumir API externa o cuando el Modelo ML no Responde
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse>handleError504(ResourceAccessException ex){
        logger.warn("TimeOut en servicio externo : {}",ex.getMessage());

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.GATEWAY_TIMEOUT.value(),
                        "Tiempo de espera Agotado",
                        "El Servicio Externo no respondio a tiempo"
                ));
    }



}


