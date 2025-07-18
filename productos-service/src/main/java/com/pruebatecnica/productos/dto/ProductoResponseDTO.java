package com.pruebatecnica.productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para responses de productos (JSON API).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ProductoResponseDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class Data {
        @Schema(description = "Tipo de recurso", example = "productos", required = true)
        private String type;

        @Schema(description = "ID del producto", example = "1", required = true)
        private String id;

        @Schema(description = "Atributos del producto", required = true)
        private Attributes attributes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class Attributes {
        @Schema(description = "Nombre del producto", example = "Laptop Gaming", required = true)
        private String nombre;

        @Schema(description = "Precio del producto", example = "1299.99", required = true)
        private BigDecimal precio;

        @Schema(description = "Descripción del producto", example = "Laptop para gaming de alta gama")
        private String descripcion;

        @Schema(description = "Fecha de creación", example = "2024-07-01T12:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "Fecha de actualización", example = "2024-07-01T12:00:00")
        private LocalDateTime updatedAt;
    }
}