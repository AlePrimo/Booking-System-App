package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Optional<Notification> getNotificationById(Long id);
    Page<Notification> getAllNotifications(Pageable pageable);
    Page<Notification> getNotificationsByRecipientId(Long userId, Pageable pageable);
    Page<Notification> getUnsentNotifications(Pageable pageable);
    void deleteNotification(Long id);
}
