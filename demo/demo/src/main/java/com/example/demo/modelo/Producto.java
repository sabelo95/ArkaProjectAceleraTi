package com.example.demo.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productos")@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
}
