package com.pruebatecnica.productos.controller;

import com.pruebatecnica.productos.dto.ProductoRequestDTO;
import com.pruebatecnica.productos.dto.ProductoResponseDTO;
import com.pruebatecnica.productos.model.ProductoEntity;
import com.pruebatecnica.productos.service.ProductoService;
import com.pruebatecnica.productos.config.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para productos (JSON API).
 */
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "API de productos (JSON API)")
public class ProductoController {
        private final ProductoService productoService;

        @Operation(summary = "Crear producto", responses = {
                        @ApiResponse(responseCode = "201", description = "Producto creado", content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content)
        })

        @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request) {
                if (!"productos".equals(request.getData().getType())) {
                        throw new IllegalArgumentException("El tipo debe ser 'productos'");
                }

                ProductoEntity entity = ProductoEntity.builder()
                                .nombre(request.getData().getAttributes().getNombre())
                                .precio(request.getData().getAttributes().getPrecio())
                                .descripcion(request.getData().getAttributes().getDescripcion())
                                .build();

                ProductoEntity creado = productoService.crearProducto(entity);
                ProductoResponseDTO response = toResponseDTO(creado);
                log.info("Producto creado con id {}", creado.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Obtener producto por id", responses = {
                        @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content)
        })
        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ProductoResponseDTO> obtenerProducto(@PathVariable Long id) {
                ProductoEntity entity = productoService.obtenerProductoPorId(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Producto no encontrado con id: " + id));
                return ResponseEntity.ok(toResponseDTO(entity));
        }

        @Operation(summary = "Listar productos paginados", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista paginada de productos", content = @Content(schema = @Schema(implementation = ProductoResponseDTO.class)))
        })
        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Object> listarProductos(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<ProductoEntity> productos = productoService.listarProductos(pageable);
                List<ProductoResponseDTO.Data> dataList = productos.getContent().stream()
                                .map(this::toResponseDTOData)
                                .collect(Collectors.toList());
                // JSON API: lista paginada en "data"
                return ResponseEntity.ok().body(
                                java.util.Map.of(
                                                "data", dataList,
                                                "meta", java.util.Map.of(
                                                                "page", page,
                                                                "size", size,
                                                                "totalElements", productos.getTotalElements(),
                                                                "totalPages", productos.getTotalPages())));
        }

        private ProductoResponseDTO toResponseDTO(ProductoEntity entity) {
                return ProductoResponseDTO.builder()
                                .data(toResponseDTOData(entity))
                                .build();
        }

        private ProductoResponseDTO.Data toResponseDTOData(ProductoEntity entity) {
                return ProductoResponseDTO.Data.builder()
                                .type("productos")
                                .id(entity.getId().toString())
                                .attributes(ProductoResponseDTO.Attributes.builder()
                                                .nombre(entity.getNombre())
                                                .precio(entity.getPrecio())
                                                .descripcion(entity.getDescripcion())
                                                .createdAt(entity.getCreatedAt())
                                                .updatedAt(entity.getUpdatedAt())
                                                .build())
                                .build();
        }
}