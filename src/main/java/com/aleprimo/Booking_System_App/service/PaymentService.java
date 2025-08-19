package com.aleprimo.Booking_System_App.service;



import com.aleprimo.Booking_System_App.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Optional<Payment> getPaymentById(Long id);
    Optional<Payment> getPaymentByBookingId(Long bookingId);
    Page<Payment> getAllPayments(Pageable pageable);
    void deletePayment(Long id);
}
