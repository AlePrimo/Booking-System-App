package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.persistence.PaymentDAO;
import com.aleprimo.Booking_System_App.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;

    @Override

    public Payment createPayment(Payment payment) {
        return paymentDAO.save(payment);
    }

    @Override

    public Optional<Payment> getPaymentById(Long id) {
        return paymentDAO.findById(id);
    }

    @Override

    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentDAO.findByBookingId(bookingId);
    }

    @Override

    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentDAO.findAll(pageable);
    }

    @Override

    public void deletePayment(Long id) {
        paymentDAO.deleteById(id);
    }
}
