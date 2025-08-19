package com.aleprimo.Booking_System_App.mapper.payment;


import com.aleprimo.Booking_System_App.dto.payment.PaymentRequestDTO;
import com.aleprimo.Booking_System_App.dto.payment.PaymentResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public  Payment toEntity(PaymentRequestDTO dto, Booking booking) {
        return Payment.builder()
                .booking(booking)
                .amount(dto.getAmount())
                .build();
    }

    public  PaymentResponseDTO toDTO(Payment entity) {
        return PaymentResponseDTO.builder()
                .id(entity.getId())
                .bookingId(entity.getBooking() != null ? entity.getBooking().getId() : null)
                .amount(entity.getAmount())
                .build();
    }
}
