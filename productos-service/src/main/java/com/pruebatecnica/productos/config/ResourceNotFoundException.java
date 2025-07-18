package com.pruebatecnica.productos.config;

/**
 * Excepción para recursos no encontrados (404).
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}