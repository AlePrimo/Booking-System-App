package com.aleprimo.Booking_System_App.mapper;

import com.aleprimo.Booking_System_App.dto.notification.NotificationRequestDTO;
import com.aleprimo.Booking_System_App.dto.notification.NotificationResponseDTO;
import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.mapper.notification.NotificationMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private final NotificationMapper mapper = new NotificationMapper();

    @Test
    void testToEntity() {
        // Arrange
        NotificationRequestDTO dto = NotificationRequestDTO.builder()
                .message("Test message")
                .build();

        User recipient = User.builder()
                .id(1L)
                .name("john_doe")
                .build();

        // Act
        Notification entity = mapper.toEntity(dto, recipient);

        // Assert
        assertNotNull(entity);
        assertEquals("Test message", entity.getMessage());
        assertEquals(recipient, entity.getRecipient());
        assertFalse(entity.isSent(), "El campo sent debería inicializarse en false");
    }

    @Test
    void testToDTO_WithRecipient() {
        // Arrange
        User recipient = User.builder()
                .id(5L)
                .name("jane_doe")
                .build();

        Notification entity = Notification.builder()
                .id(10L)
                .message("Hello World")
                .recipient(recipient)
                .sent(true)
                .type(NotificationType.EMAIL)
                .build();

        // Act
        NotificationResponseDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Hello World", dto.getMessage());
        assertEquals(5L, dto.getRecipientId());
        assertTrue(dto.isSent());
        assertEquals(NotificationType.EMAIL, dto.getType());
    }

    @Test
    void testToDTO_WithoutRecipient() {
        // Arrange
        Notification entity = Notification.builder()
                .id(20L)
                .message("No recipient")
                .recipient(null)
                .sent(false)
                .type(NotificationType.SMS)
                .build();

        // Act
        NotificationResponseDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(20L, dto.getId());
        assertEquals("No recipient", dto.getMessage());
        assertNull(dto.getRecipientId(), "Si el recipient es null, el DTO también debe ser null");
        assertFalse(dto.isSent());
        assertEquals(NotificationType.SMS, dto.getType());
    }
}
