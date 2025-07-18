package com.pruebatecnica.productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO para requests de creación/actualización de productos (JSON API).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    @NotNull(message = "El objeto data es obligatorio.")
    private ProductoData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoData {
        @Schema(description = "Tipo de recurso, debe ser 'productos'", example = "productos", required = true)
        @NotBlank(message = "El tipo es obligatorio.")
        private String type;

        @Schema(description = "Atributos del producto", required = true)
        @NotNull(message = "Los atributos son obligatorios.")
        private ProductoAttributes attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductoAttributes {
        @Schema(description = "Nombre del producto", example = "Laptop Gaming", required = true)
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
        private String nombre;

        @Schema(description = "Precio del producto", example = "1299.99", required = true)
        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0.")
        private BigDecimal precio;

        @Schema(description = "Descripción del producto", example = "Laptop para gaming de alta gama")
        @Size(max = 500, message = "La descripción no puede superar 500 caracteres.")
        private String descripcion;
    }
}