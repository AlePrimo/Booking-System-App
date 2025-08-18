package com.aleprimo.Booking_System_App.service;



import com.aleprimo.Booking_System_App.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Optional<Payment> getPaymentById(Long id);
    Optional<Payment> getPaymentByBookingId(Long bookingId);
    List<Payment> getAllPayments();
    void deletePayment(Long id);
}
