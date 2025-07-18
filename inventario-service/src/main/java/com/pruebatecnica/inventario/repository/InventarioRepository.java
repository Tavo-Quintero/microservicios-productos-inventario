package com.pruebatecnica.inventario.repository;

import com.pruebatecnica.inventario.model.InventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioEntity, Long> {
    Optional<InventarioEntity> findByProductoId(Long productoId);
}