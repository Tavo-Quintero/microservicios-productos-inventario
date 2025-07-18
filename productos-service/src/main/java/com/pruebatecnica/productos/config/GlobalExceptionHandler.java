package com.pruebatecnica.productos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador global de excepciones para respuestas JSON API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errors", ex.getBindingResult().getFieldErrors().stream().map(this::toJsonApiError).toList());
        log.warn("Error de validación: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errors", List.of(Map.of(
                "status", "404",
                "title", "Not Found",
                "detail", ex.getMessage())));
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneral(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errors", List.of(Map.of(
                "status", "500",
                "title", "Internal Server Error",
                "detail", ex.getMessage())));
        log.error("Error interno: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private Map<String, Object> toJsonApiError(FieldError fieldError) {
        return Map.of(
                "status", "400",
                "title", "Validation Error",
                "detail", fieldError.getDefaultMessage(),
                "source", Map.of("pointer", "/data/attributes/" + fieldError.getField()));
    }
}