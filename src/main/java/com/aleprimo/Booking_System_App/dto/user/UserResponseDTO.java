package com.aleprimo.Booking_System_App.dto.user;


import com.aleprimo.Booking_System_App.entity.enums.Role;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
}
