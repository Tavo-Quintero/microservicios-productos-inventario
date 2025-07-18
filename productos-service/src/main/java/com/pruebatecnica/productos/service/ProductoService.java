package com.pruebatecnica.productos.service;

import com.pruebatecnica.productos.model.ProductoEntity;
import com.pruebatecnica.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de negocio para productos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {
    private final ProductoRepository productoRepository;

    /**
     * Crea un nuevo producto.
     */
    @Transactional
    public ProductoEntity crearProducto(ProductoEntity producto) {
        log.info("Creando producto: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    /**
     * Obtiene un producto por su ID.
     */
    @Transactional(readOnly = true)
    public Optional<ProductoEntity> obtenerProductoPorId(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id);
    }

    /**
     * Lista productos paginados.
     */
    @Transactional(readOnly = true)
    public Page<ProductoEntity> listarProductos(Pageable pageable) {
        log.info("Listando productos: page {} size {}", pageable.getPageNumber(), pageable.getPageSize());
        return productoRepository.findAll(pageable);
    }
}