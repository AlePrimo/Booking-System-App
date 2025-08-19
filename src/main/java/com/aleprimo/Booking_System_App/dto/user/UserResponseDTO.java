package com.aleprimo.Booking_System_App.dto.user;


import com.aleprimo.Booking_System_App.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.com")
    private String email;

    @Schema(description = "Conjunto de roles asignados al usuario")
    private Set<Role> roles;
}
