package com.aleprimo.Booking_System_App.dto.notification;

import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    @Schema(description = "Contenido del mensaje de la notificación", example = "Tu reserva ha sido confirmada")
    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @Schema(description = "ID del usuario destinatario de la notificación", example = "5")
    @NotNull(message = "El destinatario es obligatorio")
    private Long recipientId;

    @Schema(description = "Tipo de notificación", example = "EMAIL")
    @NotNull(message = "El tipo de notificación es obligatorio")
    private NotificationType type;


}
