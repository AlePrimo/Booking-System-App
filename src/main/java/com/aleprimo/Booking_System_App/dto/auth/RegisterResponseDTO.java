package com.aleprimo.Booking_System_App.dto.auth;

import com.aleprimo.Booking_System_App.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {

    @Schema(description = "Id unico del usuario", example = "1")
    private Long id;
    @Schema(description = "Nombre del usuario", example = "juan perez")
    private String name;
    @Schema(description = "Correo electronico del usuario", example = "juanperez@mail.com")
    private String email;
    @Schema(description = "Rol del usuario autenticado", example = "ROLE_CUSTOMER")
    private Role role;

}
