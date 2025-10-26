package com.Notificaciones_service.Modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {
    @Id
    private String id;
    private String tipo;
    private String destinatario;
    private String mensaje;
    private LocalDateTime fecha;
}
