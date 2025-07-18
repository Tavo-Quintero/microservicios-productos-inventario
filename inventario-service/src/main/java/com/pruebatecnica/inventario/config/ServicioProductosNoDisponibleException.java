package com.pruebatecnica.inventario.config;

public class ServicioProductosNoDisponibleException extends RuntimeException {
    public ServicioProductosNoDisponibleException(String message) {
        super(message);
    }

    public ServicioProductosNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}