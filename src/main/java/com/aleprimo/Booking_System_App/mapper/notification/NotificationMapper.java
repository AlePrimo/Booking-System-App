package com.aleprimo.Booking_System_App.mapper.notification;


import com.aleprimo.Booking_System_App.dto.notification.NotificationRequestDTO;
import com.aleprimo.Booking_System_App.dto.notification.NotificationResponseDTO;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequestDTO dto, User recipient) {
        return Notification.builder()
                .message(dto.getMessage())
                .recipient(recipient)
                .type(dto.getType())
                .sent(false)
                .build();
    }

    public NotificationResponseDTO toDTO(Notification entity) {
        return NotificationResponseDTO.builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .recipientId(entity.getRecipient() != null ? entity.getRecipient().getId() : null)
                .sent(entity.isSent())
                .type(entity.getType())
                .build();
    }
}
