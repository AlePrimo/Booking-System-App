package com.aleprimo.Booking_System_App.persistence;


import com.aleprimo.Booking_System_App.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationDAO {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    Page<Notification> findAll(Pageable pageable);
    Page<Notification> findByRecipientId(Long userId, Pageable pageable);
    Page<Notification> findUnsent(Pageable pageable);
    void deleteById(Long id);
}
