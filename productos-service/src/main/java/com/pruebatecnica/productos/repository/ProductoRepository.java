package com.pruebatecnica.productos.repository;

import com.pruebatecnica.productos.model.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para productos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
}