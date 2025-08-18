package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.persistence.PaymentDAO;
import com.aleprimo.Booking_System_App.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;

    @Override
    @Operation(summary = "Crear un nuevo pago")
    public Payment createPayment(Payment payment) {
        return paymentDAO.save(payment);
    }

    @Override
    @Operation(summary = "Obtener pago por ID")
    public Optional<Payment> getPaymentById(Long id) {
        return paymentDAO.findById(id);
    }

    @Override
    @Operation(summary = "Obtener pago por ID de reserva")
    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentDAO.findByBookingId(bookingId);
    }

    @Override
    @Operation(summary = "Obtener todos los pagos")
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    @Override
    @Operation(summary = "Eliminar un pago")
    public void deletePayment(Long id) {
        paymentDAO.deleteById(id);
    }
}
