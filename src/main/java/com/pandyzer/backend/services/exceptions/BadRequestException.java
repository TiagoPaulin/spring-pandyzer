package com.pandyzer.backend.services.exceptions;

public class BadRequestException extends  RuntimeException {

    public BadRequestException (String msg) {
        super(msg);
    }

}
