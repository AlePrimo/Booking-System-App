package com.aleprimo.Booking_System_App.dto.login;

import com.aleprimo.Booking_System_App.entity.enums.Role;
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
    @Schema(description = "Rol del usuario autenticado", example = "ROLE_CUSTOMER")
    private Role role;   // 👈 nuevo campo

}
