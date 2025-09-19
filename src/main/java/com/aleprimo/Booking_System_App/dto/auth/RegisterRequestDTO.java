package com.aleprimo.Booking_System_App.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    @Schema(description = "Nombre del usuario", example = "juan perez")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan@mail.com")
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;


    @Schema(description = "Contraseña del usuario", example = "juanperez2025")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}
