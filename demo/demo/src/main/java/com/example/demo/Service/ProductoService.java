package com.example.demo.Service;

import com.example.demo.modelo.Producto;
import com.example.demo.repositorio.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Flux<Producto> listar() {
        return productoRepository.findAll();
    }

    public Mono<Producto> obtenerPorId(String id) {
        return productoRepository.findById(id);
    }

    public Mono<Producto> crear(Producto producto) {
        return productoRepository.save(producto);
    }

    public Mono<Void> eliminar(String id) {
        return productoRepository.deleteById(id);
    }
}
