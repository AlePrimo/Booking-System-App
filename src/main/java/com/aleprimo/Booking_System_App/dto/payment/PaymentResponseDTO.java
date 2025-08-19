package com.aleprimo.Booking_System_App.dto.payment;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long bookingId;
    private BigDecimal amount;
}
