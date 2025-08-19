package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Page<Payment> findAllPageable(Pageable pageable);
}
