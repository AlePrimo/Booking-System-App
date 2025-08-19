package com.aleprimo.Booking_System_App.dto.notification;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    private Long id;
    private String message;
    private Long recipientId;
    private boolean sent;
}
