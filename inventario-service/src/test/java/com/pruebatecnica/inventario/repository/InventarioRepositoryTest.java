package com.pruebatecnica.inventario.repository;

import com.pruebatecnica.inventario.model.InventarioEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InventarioRepositoryTest {
    @Autowired
    private InventarioRepository inventarioRepository;

    @Test
    @DisplayName("Guardar y buscar inventario por productoId")
    void guardarYBuscarInventario() {
        InventarioEntity inventario = InventarioEntity.builder()
                .productoId(1L)
                .cantidad(50)
                .build();
        InventarioEntity guardado = inventarioRepository.save(inventario);
        Optional<InventarioEntity> encontrado = inventarioRepository.findByProductoId(1L);
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCantidad()).isEqualTo(50);
    }
}