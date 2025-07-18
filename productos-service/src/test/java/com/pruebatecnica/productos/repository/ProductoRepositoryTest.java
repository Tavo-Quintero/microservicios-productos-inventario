package com.pruebatecnica.productos.repository;

import com.pruebatecnica.productos.model.ProductoEntity;
import com.pruebatecnica.productos.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para ProductoRepository.
 */
@DataJpaTest
class ProductoRepositoryTest {
    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("Guardar y buscar producto por id")
    void guardarYBuscarProducto() {
        ProductoEntity producto = ProductoEntity.builder()
                .nombre("RepoTest")
                .precio(BigDecimal.valueOf(99.99))
                .descripcion("desc repo")
                .build();
        ProductoEntity guardado = productoRepository.save(producto);
        Optional<ProductoEntity> encontrado = productoRepository.findById(guardado.getId());
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo("RepoTest");
    }
}