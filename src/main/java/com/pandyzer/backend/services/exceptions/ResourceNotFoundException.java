package com.pandyzer.backend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object obj, Object id) {
        super(obj + " não encontrado. Id: " + id);
    }

}
