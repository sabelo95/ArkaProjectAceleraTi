package com.Notificaciones_service.Repositorio;

import com.Notificaciones_service.Modelo.Notificacion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificacionRepository extends ReactiveMongoRepository<Notificacion, String> {
}
