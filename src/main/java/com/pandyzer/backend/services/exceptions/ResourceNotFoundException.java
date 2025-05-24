package com.pandyzer.backend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id, Object obj) {
        super(obj + " não encontrado. Id: " + id);
    }

}
