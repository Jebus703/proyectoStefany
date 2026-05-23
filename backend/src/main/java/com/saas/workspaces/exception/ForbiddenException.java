package com.saas.workspaces.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("No tienes permisos para realizar esta accion");
    }
}
