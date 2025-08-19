package com.aleprimo.Booking_System_App.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "La reserva es obligatoria")
    private Long bookingId;

    @Min(value = 0, message = "El monto debe ser positivo")
    private double amount;
}
