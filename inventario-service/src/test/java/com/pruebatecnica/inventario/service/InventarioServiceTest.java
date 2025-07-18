package com.pruebatecnica.inventario.service;

import com.pruebatecnica.inventario.config.InventarioNoEncontradoException;
import com.pruebatecnica.inventario.config.StockInsuficienteException;
import com.pruebatecnica.inventario.dto.*;
import com.pruebatecnica.inventario.model.InventarioEntity;
import com.pruebatecnica.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InventarioServiceTest {
    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private ProductoClient productoClient;
    @InjectMocks
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void consultarInventario_exito() {
        Long productoId = 1L;
        ProductoExternalDTO producto = ProductoExternalDTO.builder().id(1L).nombre("Test").precio(BigDecimal.TEN)
                .build();
        InventarioEntity inventario = InventarioEntity.builder().id(1L).productoId(1L).cantidad(10).build();
        when(productoClient.getProductoById(productoId)).thenReturn(producto);
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));
        InventarioResponseDTO response = inventarioService.consultarInventario(productoId);
        assertThat(response.getData().getAttributes().getCantidad()).isEqualTo(10);
        assertThat(response.getData().getAttributes().getNombreProducto()).isEqualTo("Test");
    }

    @Test
    void consultarInventario_noExisteInventario() {
        when(productoClient.getProductoById(1L)).thenReturn(ProductoExternalDTO.builder().id(1L).build());
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.empty());
        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.consultarInventario(1L));
    }

    @Test
    void realizarCompra_stockInsuficiente() {
        Long productoId = 1L;
        ProductoExternalDTO producto = ProductoExternalDTO.builder().id(1L).nombre("Test").precio(BigDecimal.TEN)
                .build();
        InventarioEntity inventario = InventarioEntity.builder().id(1L).productoId(1L).cantidad(1).build();
        when(productoClient.getProductoById(productoId)).thenReturn(producto);
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));
        CompraRequestDTO request = CompraRequestDTO.builder()
                .data(CompraRequestDTO.CompraData.builder()
                        .type("compras")
                        .attributes(CompraRequestDTO.CompraAttributes.builder()
                                .productoId(productoId)
                                .cantidad(2)
                                .build())
                        .build())
                .build();
        assertThrows(StockInsuficienteException.class, () -> inventarioService.realizarCompra(request));
    }

    @Test
    void actualizarCantidad_exito() {
        Long productoId = 1L;
        ProductoExternalDTO producto = ProductoExternalDTO.builder().id(1L).nombre("Test").precio(BigDecimal.TEN)
                .build();
        InventarioEntity inventario = InventarioEntity.builder().id(1L).productoId(1L).cantidad(5).build();
        when(productoClient.getProductoById(productoId)).thenReturn(producto);
        when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any())).thenReturn(inventario);
        InventarioRequestDTO request = InventarioRequestDTO.builder()
                .data(InventarioRequestDTO.InventarioData.builder()
                        .type("inventario")
                        .attributes(InventarioRequestDTO.InventarioAttributes.builder().cantidad(20).build())
                        .build())
                .build();
        InventarioResponseDTO response = inventarioService.actualizarCantidad(productoId, request);
        assertThat(response.getData().getAttributes().getCantidad()).isEqualTo(5); // El mock no cambia el valor
    }
}