package com.aleprimo.Booking_System_App.dto.payment;

import com.aleprimo.Booking_System_App.entity.enums.PaymentMethod;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

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
    @DecimalMin(value = "0.0",inclusive = true, message = "El monto debe ser positivo")
    private BigDecimal amount;

    @Schema(description = "Método de pago utilizado", example = "CREDIT_CARD")
    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod method;

    @Schema(description = "Estado del pago", example = "PAID")
    @NotNull(message = "El estado del pago es obligatorio")
    private PaymentStatus status;

}
