package com.saas.workspaces.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("Recurso no encontrado");
    }
}
