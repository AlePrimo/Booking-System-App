package com.aleprimo.Booking_System_App.dto.booking;


import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    @Schema(description = "ID único de la reserva", example = "10")
    private Long id;

    @Schema(description = "ID del cliente", example = "1")
    private Long customerId;

    @Schema(description = "ID del servicio reservado (offering)", example = "5")
    private Long offeringId;

    @Schema(description = "Fecha y hora de la reserva", example = "2025-09-01T10:30:00")
    private LocalDateTime bookingDateTime;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMED")
    private BookingStatus status;
}
