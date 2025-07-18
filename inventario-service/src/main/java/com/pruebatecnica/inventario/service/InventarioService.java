package com.pruebatecnica.inventario.service;

import com.pruebatecnica.inventario.dto.*;
import com.pruebatecnica.inventario.model.InventarioEntity;
import com.pruebatecnica.inventario.repository.InventarioRepository;
import com.pruebatecnica.inventario.config.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {
        private final InventarioRepository inventarioRepository;
        private final ProductoClient productoClient;

        public InventarioResponseDTO consultarInventario(Long productoId) {
                ProductoExternalDTO producto = productoClient.getProductoById(productoId);
                InventarioEntity inventario = inventarioRepository.findByProductoId(productoId)
                                .orElseThrow(() -> new InventarioNoEncontradoException(
                                                "Inventario no encontrado para productoId: " + productoId));
                return InventarioResponseDTO.builder()
                                .data(InventarioResponseDTO.Data.builder()
                                                .type("inventario")
                                                .id(inventario.getId().toString())
                                                .attributes(InventarioResponseDTO.Attributes.builder()
                                                                .productoId(productoId)
                                                                .cantidad(inventario.getCantidad())
                                                                .nombreProducto(producto.getNombre())
                                                                .precioProducto(producto.getPrecio().doubleValue())
                                                                .build())
                                                .build())
                                .build();
        }

        @Transactional
        public InventarioResponseDTO actualizarCantidad(Long productoId, InventarioRequestDTO request) {
                ProductoExternalDTO producto = productoClient.getProductoById(productoId);
                InventarioEntity inventario = inventarioRepository.findByProductoId(productoId)
                                .orElseThrow(() -> new InventarioNoEncontradoException(
                                                "Inventario no encontrado para productoId: " + productoId));
                Integer nuevaCantidad = request.getData().getAttributes().getCantidad();
                if (nuevaCantidad < 0)
                        throw new IllegalArgumentException("La cantidad no puede ser negativa");
                inventario.setCantidad(nuevaCantidad);
                inventarioRepository.save(inventario);
                return consultarInventario(productoId);
        }

        @Transactional
        public CompraResponseDTO realizarCompra(CompraRequestDTO request) {
                Long productoId = request.getData().getAttributes().getProductoId();
                Integer cantidad = request.getData().getAttributes().getCantidad();
                ProductoExternalDTO producto = productoClient.getProductoById(productoId);
                InventarioEntity inventario = inventarioRepository.findByProductoId(productoId)
                                .orElseThrow(() -> new InventarioNoEncontradoException(
                                                "Inventario no encontrado para productoId: " + productoId));
                if (cantidad <= 0)
                        throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
                if (inventario.getCantidad() < cantidad)
                        throw new StockInsuficienteException("Stock insuficiente");
                inventario.setCantidad(inventario.getCantidad() - cantidad);
                inventarioRepository.save(inventario);

                return CompraResponseDTO.builder()
                                .data(CompraResponseDTO.Data.builder()
                                                .type("compras")
                                                .id(UUID.randomUUID().toString())
                                                .attributes(CompraResponseDTO.Attributes.builder()
                                                                .productoId(productoId)
                                                                .cantidad(cantidad)
                                                                .cantidadRestante(inventario.getCantidad())
                                                                .nombreProducto(producto.getNombre())
                                                                .precioTotal(producto.getPrecio().doubleValue()
                                                                                * cantidad)
                                                                .fechaCompra(LocalDateTime.now())
                                                                .build())
                                                .build())
                                .build();
        }
}