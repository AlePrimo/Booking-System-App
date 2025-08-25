package com.aleprimo.Booking_System_App.dto.booking;

import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {

    @Schema(description = "ID del cliente que realiza la reserva", example = "1")
    @NotNull(message = "El cliente es obligatorio")
    private Long customerId;

    @Schema(description = "ID del servicio u offering reservado", example = "5")
    @NotNull(message = "El servicio es obligatorio")
    private Long offeringId;

    @Schema(description = "Fecha y hora de la reserva", example = "2025-09-01T10:30:00")
    @NotNull(message = "La fecha y hora de la reserva son obligatorias")
    @FutureOrPresent(message = "La fecha de reserva debe ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingDateTime;

    @Schema(description = "Estado de la reserva", example = "PENDING")
    @NotNull(message = "El estado es obligatorio")
    private BookingStatus status;
}
