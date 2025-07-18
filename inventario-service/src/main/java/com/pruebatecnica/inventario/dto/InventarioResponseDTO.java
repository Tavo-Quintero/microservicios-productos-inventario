package com.pruebatecnica.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponseDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    private InventarioData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventarioData {
        @Schema(description = "Tipo de recurso", example = "inventario", required = true)
        private String type;

        @Schema(description = "ID del inventario", example = "1", required = true)
        private String id;

        @Schema(description = "Atributos del inventario", required = true)
        private InventarioAttributes attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventarioAttributes {
        private Long productoId;
        private Integer cantidad;
        private String nombreProducto;
        private Double precioProducto;
    }
}