package com.Notificaciones_service.Service;

import com.Notificaciones_service.Modelo.Notificacion;
import com.Notificaciones_service.Repositorio.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository repository;
    private final EmailService emailService;

    public Mono<Notificacion> enviarNotificacion(String tipo, String destinatario, String mensaje) {
        Notificacion n = new Notificacion(null, tipo, destinatario, mensaje, LocalDateTime.now());

        emailService.enviarCorreo(destinatario, "Nueva Notificación: " + tipo, mensaje)
                .subscribe();

        return repository.save(n)
                .doOnSuccess(notif -> System.out.println("📧 Notificación enviada a " + destinatario));
    }

    public Flux<Notificacion> listarNotificaciones() {
        return repository.findAll();
    }

    public Mono<Notificacion> enviarAdjunto(String tipo,
                                            String destinatario,
                                            String mensaje,
                                            byte[] archivo,
                                            String nombreArchivo) {
        Notificacion n = new Notificacion(null, tipo, destinatario, mensaje, LocalDateTime.now());

        return Mono.fromRunnable(() -> {
                    try {
                        emailService.enviarConAdjunto(
                                destinatario,
                                "Nueva Notificación: " + tipo,
                                mensaje,
                                archivo,
                                nombreArchivo
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Error enviando correo con adjunto", e);
                    }
                })
                .then(repository.save(n))
                .doOnSuccess(notif -> System.out.println("📧 Notificación enviada a " + destinatario));
    }
}

