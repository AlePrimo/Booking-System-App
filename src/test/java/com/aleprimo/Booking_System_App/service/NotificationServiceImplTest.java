package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.persistence.NotificationDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationDAO notificationDAO;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User recipient;
    private Notification notification;

    @BeforeEach
    void setUp() {
        recipient = User.builder()
                .id(1L)
                .name("Juan")
                .email("juan@mail.com")
                .password("1234")
                .roles(Set.of(Role.CUSTOMER))
                .build();

        notification = Notification.builder()
                .id(1L)
                .type(NotificationType.EMAIL)
                .message("Test Message")
                .recipient(recipient)
                .sent(false)
                .build();
    }

    @Test
    void testCreateNotification() {
        when(notificationDAO.save(notification)).thenReturn(notification);

        Notification created = notificationService.createNotification(notification);

        assertThat(created).isNotNull();
        assertThat(created.getMessage()).isEqualTo("Test Message");
        verify(notificationDAO, times(1)).save(notification);
    }

    @Test
    void testGetNotificationById() {
        when(notificationDAO.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> found = notificationService.getNotificationById(1L);

        assertThat(found).isPresent();
    }

    @Test
    void testGetAllNotifications() {
        when(notificationDAO.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(notification)));

        Page<Notification> page = notificationService.getAllNotifications(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void testGetNotificationsByRecipientId() {
        when(notificationDAO.findByRecipientId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(notification)));

        Page<Notification> page = notificationService.getNotificationsByRecipientId(1L, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getRecipient().getId()).isEqualTo(1L);
    }

    @Test
    void testGetUnsentNotifications() {
        when(notificationDAO.findUnsent(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(notification)));

        Page<Notification> page = notificationService.getUnsentNotifications(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).isSent()).isFalse();
    }

    @Test
    void testDeleteNotification() {
        notificationService.deleteNotification(1L);
        verify(notificationDAO, times(1)).deleteById(1L);
    }
}
