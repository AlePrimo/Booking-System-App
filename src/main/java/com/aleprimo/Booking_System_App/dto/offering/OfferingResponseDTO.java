package com.aleprimo.Booking_System_App.dto.offering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingResponseDTO {
    @Schema(description = "ID único del servicio o producto", example = "7")
    private Long id;

    @Schema(description = "Nombre del servicio o producto ofrecido", example = "Corte de cabello")
    private String name;

    @Schema(description = "Descripción del servicio o producto", example = "Corte de cabello con lavado incluido")
    private String description;

    @Schema(description = "Duración del servicio en minutos", example = "45")
    private int durationMinutes;

    @Schema(description = "Precio del servicio o producto", example = "1500.00")
    private BigDecimal price;

    @Schema(description = "ID del proveedor que ofrece el servicio", example = "3")
    private Long providerId;
}
