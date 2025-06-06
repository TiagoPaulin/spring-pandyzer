package com.pandyzer.backend.controllers.exceptions;

import com.pandyzer.backend.chat.services.exceptions.GeminiException;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {

        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandartError standartError = new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(standartError);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandartError> badRequest(BadRequestException e, HttpServletRequest request) {

        String error = "Data Error";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandartError standartError = new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(standartError);

    }

    @ExceptionHandler(GeminiException.class)
    public ResponseEntity<StandartError> geminiException(GeminiException e, HttpServletRequest request) {

        String error = "API Error";
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        StandartError standartError = new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(standartError);

    }

}
