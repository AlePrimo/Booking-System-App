package com.aleprimo.Booking_System_App.dto.offering;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int durationMinutes;

    @Min(value = 0, message = "El precio debe ser positivo")
    private double price;

    @NotNull(message = "El proveedor es obligatorio")
    private Long providerId;
}
