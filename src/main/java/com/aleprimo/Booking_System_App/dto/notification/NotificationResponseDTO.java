package com.aleprimo.Booking_System_App.dto.notification;

import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    @Schema(description = "ID único de la notificación", example = "12")
    private Long id;

    @Schema(description = "Contenido del mensaje de la notificación", example = "Tu reserva ha sido confirmada")
    private String message;

    @Schema(description = "ID del usuario destinatario de la notificación", example = "5")
    private Long recipientId;

    @Schema(description = "Tipo de notificación", example = "EMAIL")
    private NotificationType type;
    @Schema(description = "Indica si la notificación ya fue enviada", example = "true")
    private boolean sent;
}
