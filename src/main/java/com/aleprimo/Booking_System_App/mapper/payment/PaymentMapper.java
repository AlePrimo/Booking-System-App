package com.aleprimo.Booking_System_App.mapper.payment;


import com.aleprimo.Booking_System_App.dto.payment.PaymentRequestDTO;
import com.aleprimo.Booking_System_App.dto.payment.PaymentResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto, Booking booking) {
        return Payment.builder()
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .booking(booking)
                .build();
    }

    public PaymentResponseDTO toDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .build();
    }
}
