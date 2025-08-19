package com.aleprimo.Booking_System_App.dto.booking;

import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    private Long customerId;

    @NotNull(message = "El servicio es obligatorio")
    private Long offeringId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDateTime bookingDateTime;

    private BookingStatus status = BookingStatus.PENDING;
}
