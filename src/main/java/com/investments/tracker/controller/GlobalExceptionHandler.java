package com.investments.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", Instant.now());
        errorBody.put("status", ex.getStatusCode().value());
        errorBody.put("error", ex.getReason());
        errorBody.put("message", ex.getReason());

        return new ResponseEntity<>(errorBody, ex.getStatusCode());
    }

}

