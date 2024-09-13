package com.dws.challenge.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest){
        Map<String , Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handlerAccountNotFoundException(AccountNotFoundException exception, WebRequest webRequest){
        Map<String , Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(errors.toString());
    }
}
