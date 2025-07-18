package com.pruebatecnica.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraRequestDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    @NotNull
    private Data data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        @Schema(description = "Tipo de recurso", example = "compras", required = true)
        @NotBlank
        private String type;

        @Schema(description = "Atributos de la compra", required = true)
        @NotNull
        private Attributes attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Attributes {
        @Schema(description = "ID del producto", required = true)
        @NotNull
        @Min(1)
        private Long productoId;

        @Schema(description = "Cantidad a comprar", required = true)
        @NotNull
        @Min(1)
        private Integer cantidad;
    }
}