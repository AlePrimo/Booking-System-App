package com.aleprimo.Booking_System_App.persistence;

import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.persistence.daoImpl.NotificationDAOImpl;
import com.aleprimo.Booking_System_App.repository.NotificationRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDAOImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationDAOImpl notificationDAO;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
    }

    @Test
    void save_ShouldReturnSavedNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationDAO.save(notification);

        assertThat(result).isEqualTo(notification);
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void findById_ShouldReturnNotification() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationDAO.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(notification);
    }

    @Test
    void findAll_ShouldReturnPageOfNotifications() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(notificationRepository.findAll(pageable)).thenReturn(page);

        Page<Notification> result = notificationDAO.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(notification);
    }

    @Test
    void findByRecipientId_ShouldReturnPageOfNotifications() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(notificationRepository.findByRecipientId(10L, pageable)).thenReturn(page);

        Page<Notification> result = notificationDAO.findByRecipientId(10L, pageable);

        assertThat(result.getContent()).contains(notification);
    }

    @Test
    void findUnsent_ShouldReturnUnsentNotifications() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(notificationRepository.findBySentFalse(pageable)).thenReturn(page);

        Page<Notification> result = notificationDAO.findUnsent(pageable);

        assertThat(result.getContent()).contains(notification);
    }

    @Test
    void deleteById_ShouldInvokeRepository() {
        notificationDAO.deleteById(1L);
        verify(notificationRepository, times(1)).deleteById(1L);
    }
}
