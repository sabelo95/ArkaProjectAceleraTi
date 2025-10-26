package com.Notificaciones_service.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmailService {


    private JavaMailSender mailSender;

    public Mono<Void> enviarCorreo(String destinatario, String asunto, String cuerpoHtml) {
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(destinatario);
                helper.setSubject(asunto);
                helper.setText(cuerpoHtml, true);
                helper.setFrom("santiagoberriolopez@gmail.com");

                mailSender.send(message);
            } catch (Exception e) {
                throw new RuntimeException("Error al enviar correo HTML: " + e.getMessage(), e);
            }
        });
    }

    public void enviarConAdjunto(String destinatario, String asunto, String mensaje, byte[] archivo, String nombreArchivo) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(mensaje);
        helper.addAttachment(nombreArchivo, new ByteArrayResource(archivo));

        mailSender.send(mimeMessage);
    }
}
