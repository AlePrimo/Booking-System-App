package com.aleprimo.Booking_System_App.dto.payment;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long bookingId;
    private double amount;
}
