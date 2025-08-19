package com.aleprimo.Booking_System_App.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @Schema(description = "ID de la reserva asociada al pago", example = "15")
    @NotNull(message = "La reserva es obligatoria")
    private Long bookingId;

    @Schema(description = "Monto total del pago", example = "2500.00")
    @Min(value = 0, message = "El monto debe ser positivo")
    private BigDecimal amount;
}
