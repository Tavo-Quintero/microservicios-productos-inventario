package com.pruebatecnica.inventario.controller;

import com.pruebatecnica.inventario.dto.*;
import com.pruebatecnica.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "API de inventario (JSON API)")
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(summary = "Consultar inventario de un producto", responses = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado", content = @Content),
            @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
    })
    @GetMapping("/{productoId}")
    public ResponseEntity<InventarioResponseDTO> consultarInventario(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.consultarInventario(productoId));
    }

    @Operation(summary = "Actualizar cantidad de inventario", responses = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado", content = @Content)
    })
    @PutMapping("/{productoId}")
    public ResponseEntity<InventarioResponseDTO> actualizarCantidad(
            @PathVariable Long productoId,
            @Valid @RequestBody InventarioRequestDTO request) {
        return ResponseEntity.ok(inventarioService.actualizarCantidad(productoId, request));
    }

    @Operation(summary = "Realizar compra", responses = {
            @ApiResponse(responseCode = "201", description = "Compra realizada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o producto no existe", content = @Content)
    })
    @PostMapping("/compras")
    public ResponseEntity<CompraResponseDTO> realizarCompra(
            @Valid @RequestBody CompraRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.realizarCompra(request));
    }
}