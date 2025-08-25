package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User recipient;

    @BeforeEach
    void setUp() {
        recipient = User.builder()
                .name("Juan")
                .email("juan@mail.com")
                .password("1234")
                .roles(Set.of(Role.CUSTOMER))
                .build();
        recipient = userRepository.save(recipient);
    }

    @Test
    void testSaveAndFindById() {
        Notification notif = Notification.builder()
                .type(NotificationType.EMAIL)
                .message("Test Message")
                .recipient(recipient)
                .build();

        Notification saved = notificationRepository.save(notif);

        assertThat(notificationRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void testFindAll() {
        Notification notif = Notification.builder()
                .type(NotificationType.EMAIL)
                .message("Test Message")
                .recipient(recipient)
                .build();

        notificationRepository.save(notif);

        Page<Notification> page = notificationRepository.findAll(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testFindByRecipientId() {
        Notification notif = Notification.builder()
                .type(NotificationType.SMS)
                .message("SMS Message")
                .recipient(recipient)
                .build();

        notificationRepository.save(notif);

        Page<Notification> page = notificationRepository.findByRecipientId(recipient.getId(), PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getRecipient().getId()).isEqualTo(recipient.getId());
    }

    @Test
    void testFindBySentFalse() {
        Notification notif = Notification.builder()
                .type(NotificationType.EMAIL)
                .message("Pending Notification")
                .recipient(recipient)
                .sent(false)
                .build();

        notificationRepository.save(notif);

        Page<Notification> page = notificationRepository.findBySentFalse(PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void testDeleteById() {
        Notification notif = Notification.builder()
                .type(NotificationType.SMS)
                .message("Delete Me")
                .recipient(recipient)
                .build();

        Notification saved = notificationRepository.save(notif);
        notificationRepository.deleteById(saved.getId());

        assertThat(notificationRepository.findById(saved.getId())).isNotPresent();
    }
}
