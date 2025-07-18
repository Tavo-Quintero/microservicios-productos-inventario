package com.pruebatecnica.inventario.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
                return ResponseEntity.badRequest().body(Map.of(
                                "errors", ex.getBindingResult().getFieldErrors().stream().map(error -> Map.of(
                                                "status", "400",
                                                "title", "Validation Error",
                                                "detail", error.getDefaultMessage(),
                                                "source", Map.of("pointer", "/data/attributes/" + error.getField())))
                                                .toList()));
        }

        @ExceptionHandler(ProductoNoEncontradoException.class)
        public ResponseEntity<Object> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "errors", List.of(Map.of(
                                                "status", "404",
                                                "title", "Producto no encontrado",
                                                "detail", ex.getMessage()))));
        }

        @ExceptionHandler(InventarioNoEncontradoException.class)
        public ResponseEntity<Object> handleInventarioNoEncontrado(InventarioNoEncontradoException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "errors", List.of(Map.of(
                                                "status", "404",
                                                "title", "Inventario no encontrado",
                                                "detail", ex.getMessage()))));
        }

        @ExceptionHandler(StockInsuficienteException.class)
        public ResponseEntity<Object> handleStockInsuficiente(StockInsuficienteException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                                "errors", List.of(Map.of(
                                                "status", "400",
                                                "title", "Stock insuficiente",
                                                "detail", ex.getMessage()))));
        }

        @ExceptionHandler(ServicioProductosNoDisponibleException.class)
        public ResponseEntity<Object> handleServicioNoDisponible(ServicioProductosNoDisponibleException ex) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                                "errors", List.of(Map.of(
                                                "status", "503",
                                                "title", "Servicio productos no disponible",
                                                "detail", ex.getMessage()))));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGeneral(Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                                "errors", List.of(Map.of(
                                                "status", "500",
                                                "title", "Internal Server Error",
                                                "detail", ex.getMessage()))));
        }
}