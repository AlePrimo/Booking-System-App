package com.aleprimo.Booking_System_App.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {


    private Long id;
    private String name;
    private String email;
}
