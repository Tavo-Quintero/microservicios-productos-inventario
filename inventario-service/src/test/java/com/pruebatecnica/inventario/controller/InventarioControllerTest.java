package com.pruebatecnica.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebatecnica.inventario.dto.*;
import com.pruebatecnica.inventario.service.InventarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private InventarioService inventarioService;

        @Test
        @DisplayName("GET /api/inventario/{productoId} - éxito")
        void consultarInventario_exito() throws Exception {
                InventarioResponseDTO response = InventarioResponseDTO.builder()
                                .data(InventarioResponseDTO.InventarioData.builder()
                                                .type("inventario")
                                                .id("1")
                                                .attributes(InventarioResponseDTO.InventarioAttributes.builder()
                                                                .productoId(1L)
                                                                .cantidad(10)
                                                                .nombreProducto("Test")
                                                                .precioProducto(100.0)
                                                                .build())
                                                .build())
                                .build();
                Mockito.when(inventarioService.consultarInventario(1L)).thenReturn(response);
                mockMvc.perform(get("/api/inventario/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.type").value("inventario"))
                                .andExpect(jsonPath("$.data.attributes.cantidad").value(10));
        }

        @Test
        @DisplayName("PUT /api/inventario/{productoId} - éxito")
        void actualizarCantidad_exito() throws Exception {
                InventarioRequestDTO request = InventarioRequestDTO.builder()
                                .data(InventarioRequestDTO.InventarioData.builder()
                                                .type("inventario")
                                                .attributes(InventarioRequestDTO.InventarioAttributes.builder()
                                                                .cantidad(20).build())
                                                .build())
                                .build();
                InventarioResponseDTO response = InventarioResponseDTO.builder()
                                .data(InventarioResponseDTO.InventarioData.builder()
                                                .type("inventario")
                                                .id("1")
                                                .attributes(InventarioResponseDTO.InventarioAttributes.builder()
                                                                .productoId(1L).cantidad(20).build())
                                                .build())
                                .build();
                Mockito.when(inventarioService.actualizarCantidad(any(Long.class), any(InventarioRequestDTO.class)))
                                .thenReturn(response);
                mockMvc.perform(put("/api/inventario/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.attributes.cantidad").value(20));
        }

        @Test
        @DisplayName("POST /api/inventario/compras - éxito")
        void realizarCompra_exito() throws Exception {
                CompraRequestDTO request = CompraRequestDTO.builder()
                                .data(CompraRequestDTO.CompraData.builder()
                                                .type("compras")
                                                .attributes(CompraRequestDTO.CompraAttributes.builder().productoId(1L)
                                                                .cantidad(2).build())
                                                .build())
                                .build();
                CompraResponseDTO response = CompraResponseDTO.builder()
                                .data(CompraResponseDTO.CompraData.builder()
                                                .type("compras")
                                                .id(UUID.randomUUID().toString())
                                                .attributes(CompraResponseDTO.CompraAttributes.builder().productoId(1L)
                                                                .cantidad(2).cantidadRestante(8).nombreProducto("Test")
                                                                .precioTotal(200.0).build())
                                                .build())
                                .build();
                Mockito.when(inventarioService.realizarCompra(any(CompraRequestDTO.class))).thenReturn(response);
                mockMvc.perform(post("/api/inventario/compras")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.type").value("compras"))
                                .andExpect(jsonPath("$.data.attributes.cantidadRestante").value(8));
        }
}