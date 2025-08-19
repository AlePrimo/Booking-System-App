package com.aleprimo.Booking_System_App.dto.offering;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingRequestDTO {

    @Schema(description = "Nombre del servicio o producto ofrecido", example = "Corte de cabello")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Descripción del servicio o producto", example = "Corte de cabello con lavado incluido")
    private String description;

    @Schema(description = "Duración del servicio en minutos", example = "45")
    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int durationMinutes;

    @Schema(description = "Precio del servicio o producto", example = "1500.00")
    @Min(value = 0, message = "El precio debe ser positivo")
    private BigDecimal price;

    @Schema(description = "ID del proveedor que ofrece el servicio", example = "3")
    @NotNull(message = "El proveedor es obligatorio")
    private Long providerId;
}
