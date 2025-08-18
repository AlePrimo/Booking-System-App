package com.aleprimo.Booking_System_App.persistence.daoImpl;


import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.persistence.NotificationDAO;
import com.aleprimo.Booking_System_App.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationDAOImpl implements NotificationDAO {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification>findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Page<Notification> findByRecipientId(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }

    @Override
    public Page<Notification> findUnsent(Pageable pageable) {
        return notificationRepository.findBySentFalse(pageable);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }
}
