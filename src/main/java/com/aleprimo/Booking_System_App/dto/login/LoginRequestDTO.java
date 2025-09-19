package com.aleprimo.Booking_System_App.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class LoginRequestDTO {

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.com")
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Schema(description = "Contraseña de acceso", example = "password123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
