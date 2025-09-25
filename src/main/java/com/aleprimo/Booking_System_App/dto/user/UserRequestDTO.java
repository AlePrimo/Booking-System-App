package com.aleprimo.Booking_System_App.dto.user;


import com.aleprimo.Booking_System_App.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@mail.com")
    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Schema(description = "Contraseña de acceso del usuario", example = "password123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Schema(description = "Rol asignado al usuario")
    @NotNull(message = "El rol es obligatorio")
    private Role role;

}
