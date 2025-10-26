package com.example.demo.controller;

import com.example.demo.Service.ProductoService;
import com.example.demo.modelo.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public Flux<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public Mono<Producto> obtenerPorId(@PathVariable String id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public Mono<Producto> crear(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable String id) {
        return productoService.eliminar(id);
    }
}
