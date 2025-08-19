package com.aleprimo.Booking_System_App.dto.offering;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingResponseDTO {

    private Long id;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
    private Long providerId;
}
