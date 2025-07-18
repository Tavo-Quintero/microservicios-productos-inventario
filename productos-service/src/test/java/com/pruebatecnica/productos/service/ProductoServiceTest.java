package com.pruebatecnica.productos.service;

import com.pruebatecnica.productos.repository.ProductoRepository;
import com.pruebatecnica.productos.model.ProductoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para ProductoService.
 */
class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearProducto_deberiaRetornarProductoCreado() {
        ProductoEntity producto = ProductoEntity.builder()
                .nombre("Test")
                .precio(BigDecimal.valueOf(10))
                .descripcion("desc")
                .build();
        when(productoRepository.save(any())).thenReturn(producto);
        ProductoEntity creado = productoService.crearProducto(producto);
        assertThat(creado).isNotNull();
        assertThat(creado.getNombre()).isEqualTo("Test");
    }

    @Test
    void obtenerProductoPorId_existente_deberiaRetornarProducto() {
        ProductoEntity producto = ProductoEntity.builder().nombre("Test").precio(BigDecimal.ONE).build();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Optional<ProductoEntity> resultado = productoService.obtenerProductoPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Test");
    }

    @Test
    void obtenerProductoPorId_noExistente_deberiaRetornarVacio() {
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<ProductoEntity> resultado = productoService.obtenerProductoPorId(2L);
        assertThat(resultado).isEmpty();
    }

    @Test
    void listarProductos_deberiaRetornarPagina() {
        ProductoEntity producto = ProductoEntity.builder().nombre("Test").precio(BigDecimal.ONE).build();
        Pageable pageable = PageRequest.of(0, 10);
        when(productoRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(producto)));
        Page<ProductoEntity> pagina = productoService.listarProductos(pageable);
        assertThat(pagina.getContent()).hasSize(1);
        assertThat(pagina.getContent().get(0).getNombre()).isEqualTo("Test");
    }
}