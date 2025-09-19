package com.aleprimo.Booking_System_App.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    @Schema(description = "Access token JWT válido", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
    @Schema(description = "Refresh token para renovar el access token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;
}
