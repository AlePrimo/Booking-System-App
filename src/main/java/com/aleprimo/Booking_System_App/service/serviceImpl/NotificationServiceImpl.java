package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Notification;
import com.aleprimo.Booking_System_App.persistence.NotificationDAO;
import com.aleprimo.Booking_System_App.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDAO notificationDAO;

    @Override

    public Notification createNotification(Notification notification) {
        return notificationDAO.save(notification);
    }

    @Override

    public Optional<Notification> getNotificationById(Long id) {
        return notificationDAO.findById(id);
    }

    @Override

    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationDAO.findAll(pageable);
    }

    @Override

    public Page<Notification> getNotificationsByRecipientId(Long userId, Pageable pageable) {
        return notificationDAO.findByRecipientId(userId, pageable);
    }

    @Override

    public Page<Notification> getUnsentNotifications(Pageable pageable) {
        return notificationDAO.findUnsent(pageable);
    }

    @Override

    public void deleteNotification(Long id) {
        notificationDAO.deleteById(id);
    }

}
