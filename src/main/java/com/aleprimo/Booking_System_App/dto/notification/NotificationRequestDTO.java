package com.aleprimo.Booking_System_App.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @NotNull(message = "El destinatario es obligatorio")
    private Long recipientId;

    private boolean sent = false;
}
