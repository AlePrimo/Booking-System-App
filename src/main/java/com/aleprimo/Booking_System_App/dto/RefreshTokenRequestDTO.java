package com.aleprimo.Booking_System_App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDTO {

    @Schema(description = "Refresh token válido para renovar el access token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
