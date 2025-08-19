package com.aleprimo.Booking_System_App.persistence;



import com.aleprimo.Booking_System_App.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentDAO {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByBookingId(Long bookingId);
    Page<Payment> findAll(Pageable pageable);
    void deleteById(Long id);
}
