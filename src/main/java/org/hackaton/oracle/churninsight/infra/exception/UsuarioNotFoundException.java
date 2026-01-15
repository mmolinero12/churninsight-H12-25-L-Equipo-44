package org.hackaton.oracle.churninsight.infra.Exception;

public class UsuarioNotFoundException extends  RuntimeException{

    public UsuarioNotFoundException(Long id){
        super("Usuario con id "+id +" no encontrado");
    }

    public UsuarioNotFoundException(String mensaje){
        super(mensaje);
    }
}
