package com.pruebatecnica.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioRequestDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    @NotNull
    private InventarioData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventarioData {
        @Schema(description = "Tipo de recurso", example = "inventario", required = true)
        @NotBlank
        private String type;

        @Schema(description = "Atributos del inventario", required = true)
        @NotNull
        private InventarioAttributes attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventarioAttributes {
        @Schema(description = "Cantidad disponible", required = true)
        @NotNull
        @Min(0)
        private Integer cantidad;
    }
}