package com.aleprimo.Booking_System_App.dto.payment;

import com.aleprimo.Booking_System_App.entity.enums.PaymentMethod;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    @Schema(description = "ID único del pago", example = "101")
    private Long id;

    @Schema(description = "ID de la reserva asociada al pago", example = "15")
    private Long bookingId;

    @Schema(description = "Monto total del pago", example = "2500.00")
    private BigDecimal amount;



    @Schema(description = "Método de pago utilizado", example = "CREDIT_CARD")
    private PaymentMethod method;

    @Schema(description = "Estado del pago", example = "PAID")
    private PaymentStatus status;



}
