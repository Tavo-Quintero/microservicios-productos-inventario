package com.pruebatecnica.inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraResponseDTO {
    @Schema(description = "Objeto data de JSON API", required = true)
    private Data data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Data {
        @Schema(description = "Tipo de recurso", example = "compras", required = true)
        private String type;

        @Schema(description = "ID de la compra", example = "uuid-compra", required = true)
        private String id;

        @Schema(description = "Atributos de la compra", required = true)
        private Attributes attributes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Attributes {
        private Long productoId;
        private Integer cantidad;
        private Integer cantidadRestante;
        private String nombreProducto;
        private Double precioTotal;
        private LocalDateTime fechaCompra;
    }
}