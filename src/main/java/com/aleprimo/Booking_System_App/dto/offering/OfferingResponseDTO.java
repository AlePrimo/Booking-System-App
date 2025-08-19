package com.aleprimo.Booking_System_App.dto.offering;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingResponseDTO {

    private Long id;
    private String name;
    private String description;
    private int durationMinutes;
    private BigDecimal price;
    private Long providerId;
}
