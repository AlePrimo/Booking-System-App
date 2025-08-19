package com.aleprimo.Booking_System_App.mapper.user;


import com.aleprimo.Booking_System_App.dto.user.UserRequestDTO;
import com.aleprimo.Booking_System_App.dto.user.UserResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public  User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .roles(dto.getRoles())
                .build();
    }

    public  UserResponseDTO toDTO(User entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .build();
    }
}
