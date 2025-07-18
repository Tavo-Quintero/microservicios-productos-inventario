package com.pruebatecnica.inventario.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoExternalDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
}