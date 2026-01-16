package com.example.toyapsback.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalAccessException.class)
    public ResponseEntity<?> handleIllegalAccessException(IllegalAccessException e) {
        Map<String, Object> response = Map.of("message", "Please run the simulate first.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        Map<String, Object> response = Map.of("message", "No value present");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
