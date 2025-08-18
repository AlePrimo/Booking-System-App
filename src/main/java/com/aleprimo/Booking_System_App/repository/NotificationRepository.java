package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipientId(Long userId, Pageable pageable);

    Page<Notification> findBySentFalse(Pageable pageable);
}
