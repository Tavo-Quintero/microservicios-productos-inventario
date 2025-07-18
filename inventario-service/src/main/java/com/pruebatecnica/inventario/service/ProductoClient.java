package com.pruebatecnica.inventario.service;

import com.pruebatecnica.inventario.dto.ProductoExternalDTO;
import com.pruebatecnica.inventario.config.ProductoNoEncontradoException;
import com.pruebatecnica.inventario.config.ServicioProductosNoDisponibleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductoClient {
    private final WebClient webClient;

    @Value("${microservices.productos.base-url}")
    private String productosBaseUrl;

    @Value("${microservices.productos.timeout}")
    private int timeout;

    @Value("${microservices.productos.retry-attempts}")
    private int retryAttempts;

    public ProductoExternalDTO getProductoById(Long id) {
        try {
            return webClient.get()
                    .uri(productosBaseUrl + "/api/productos/{id}", id)
                    .retrieve()
                    .bodyToMono(ProductoExternalDTO.class)
                    .timeout(Duration.ofMillis(timeout))
                    .retry(retryAttempts)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado");
        } catch (Exception e) {
            throw new ServicioProductosNoDisponibleException("Error comunicando con productos-service", e);
        }
    }
}