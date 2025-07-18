package com.pruebatecnica.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pruebatecnica.productos.service.ProductoService;
import com.pruebatecnica.productos.dto.ProductoRequestDTO;
import com.pruebatecnica.productos.model.ProductoEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para ProductoController.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private ProductoService productoService;

        @Test
        @DisplayName("POST /api/productos - éxito")
        void crearProducto_exito() throws Exception {
                ProductoRequestDTO request = ProductoRequestDTO.builder()
                                .data(ProductoRequestDTO.ProductoData.builder()
                                                .type("productos")
                                                .attributes(ProductoRequestDTO.ProductoAttributes.builder()
                                                                .nombre("Test")
                                                                .precio(BigDecimal.valueOf(10))
                                                                .descripcion("desc")
                                                                .build())
                                                .build())
                                .build();
                ProductoEntity entity = ProductoEntity.builder().id(1L).nombre("Test").precio(BigDecimal.valueOf(10))
                                .descripcion("desc").build();
                Mockito.when(productoService.crearProducto(any())).thenReturn(entity);
                mockMvc.perform(post("/api/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.type").value("productos"))
                                .andExpect(jsonPath("$.data.attributes.nombre").value("Test"));
        }

        @Test
        @DisplayName("GET /api/productos/{id} - éxito")
        void obtenerProducto_exito() throws Exception {
                ProductoEntity entity = ProductoEntity.builder().id(1L).nombre("Test").precio(BigDecimal.ONE).build();
                Mockito.when(productoService.obtenerProductoPorId(1L)).thenReturn(Optional.of(entity));
                mockMvc.perform(get("/api/productos/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value("1"))
                                .andExpect(jsonPath("$.data.attributes.nombre").value("Test"));
        }

        @Test
        @DisplayName("GET /api/productos/{id} - no encontrado")
        void obtenerProducto_noEncontrado() throws Exception {
                Mockito.when(productoService.obtenerProductoPorId(99L)).thenReturn(Optional.empty());
                mockMvc.perform(get("/api/productos/99"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.errors[0].status").value("404"));
        }

        @Test
        @DisplayName("GET /api/productos - paginado")
        void listarProductos_paginado() throws Exception {
                ProductoEntity entity = ProductoEntity.builder().id(1L).nombre("Test").precio(BigDecimal.ONE).build();
                Mockito.when(productoService.listarProductos(any(Pageable.class)))
                                .thenReturn(new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1));
                mockMvc.perform(get("/api/productos?page=0&size=10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].id").value("1"))
                                .andExpect(jsonPath("$.meta.totalElements").value(1));
        }
}