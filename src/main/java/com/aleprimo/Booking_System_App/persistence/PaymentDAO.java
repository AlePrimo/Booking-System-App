package com.aleprimo.Booking_System_App.persistence;



import com.aleprimo.Booking_System_App.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentDAO {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findAll();
    void deleteById(Long id);
}
