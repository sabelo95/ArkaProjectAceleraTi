package com.Notificaciones_service.Request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public  class NotificacionRequest {
        private String tipo;
        private String destinatario;
        private String mensaje;
        private MultipartFile archivo;
    }

