package com.aleprimo.Booking_System_App.dto.booking;


import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {

    private Long id;
    private Long customerId;
    private Long offeringId;
    private LocalDateTime bookingDateTime;
    private BookingStatus status;
}
