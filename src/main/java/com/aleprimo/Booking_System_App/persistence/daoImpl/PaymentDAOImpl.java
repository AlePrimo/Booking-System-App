package com.aleprimo.Booking_System_App.persistence.daoImpl;


import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.persistence.PaymentDAO;
import com.aleprimo.Booking_System_App.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentDAOImpl implements PaymentDAO {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
