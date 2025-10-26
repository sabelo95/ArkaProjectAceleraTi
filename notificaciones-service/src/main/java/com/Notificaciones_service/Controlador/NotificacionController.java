package com.Notificaciones_service.Controlador;

import com.Notificaciones_service.Request.NotificacionRequest;
import com.Notificaciones_service.Service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    public Mono<String> enviarNotificacion(@RequestBody NotificacionRequest request) {
        if (request.getDestinatario() == null || request.getDestinatario().isBlank()) {
            return Mono.error(new IllegalArgumentException("El destinatario no puede ser nulo o vacío"));
        }

        return notificacionService.enviarNotificacion(
                        request.getTipo(),
                        request.getDestinatario(),
                        request.getMensaje()
                )
                .then(Mono.just("Notificación enviada correctamente a " + request.getDestinatario()))
                .onErrorResume(e -> Mono.just("Error al enviar notificación: " + e.getMessage()));
    }

    @PostMapping(path = "/con-adjunto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> enviarNotificacionConAdjunto(
            @RequestPart("tipo") String tipo,
            @RequestPart("destinatario") String destinatario,
            @RequestPart("mensaje") String mensaje,
            @RequestPart(value = "archivo", required = false) FilePart archivo) {

        if (destinatario == null || destinatario.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body("El destinatario no puede ser nulo o vacío"));
        }

        if (archivo != null) {
            return archivo.content()
                    .reduce(new byte[0], (acum, buffer) -> {
                        byte[] bytes = new byte[buffer.readableByteCount()];
                        buffer.read(bytes);
                        byte[] combined = new byte[acum.length + bytes.length];
                        System.arraycopy(acum, 0, combined, 0, acum.length);
                        System.arraycopy(bytes, 0, combined, acum.length, bytes.length);
                        return combined;
                    })
                    .flatMap(contenido ->
                            notificacionService.enviarAdjunto(tipo, destinatario, mensaje, contenido, archivo.filename())
                                    .thenReturn(ResponseEntity.ok("Correo enviado con adjunto: " + archivo.filename()))
                    )
                    .onErrorResume(e ->
                            Mono.just(ResponseEntity.internalServerError()
                                    .body("Error enviando correo: " + e.getMessage())));
        }

        // Si no hay archivo adjunto
        return notificacionService.enviarAdjunto(tipo, destinatario, mensaje, null, null)
                .thenReturn(ResponseEntity.ok("Correo enviado sin adjunto"))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.internalServerError()
                                .body("Error enviando correo: " + e.getMessage())));
    }
}
