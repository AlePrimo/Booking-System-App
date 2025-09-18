package com.aleprimo.Booking_System_App.mapper;

import com.aleprimo.Booking_System_App.dto.user.UserRequestDTO;
import com.aleprimo.Booking_System_App.dto.user.UserResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.user.UserMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void testToEntity() {
        // Arrange
        UserRequestDTO dto = UserRequestDTO.builder()
                .name("Alejandro")
                .email("ale@example.com")
                .password("securePass")
                .roles(Set.of(Role.ROLE_CUSTOMER, Role.ROLE_ADMIN))
                .build();

        // Act
        User entity = userMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("Alejandro", entity.getName());
        assertEquals("ale@example.com", entity.getEmail());
        assertEquals("securePass", entity.getPassword());
        assertTrue(entity.getRoles().contains(Role.ROLE_CUSTOMER));
        assertTrue(entity.getRoles().contains(Role.ROLE_ADMIN));
    }

    @Test
    void testToDTO() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("Primo")
                .email("primo@example.com")
                .password("hiddenPass")
                .roles(Set.of(Role.ROLE_CUSTOMER))
                .build();

        // Act
        UserResponseDTO dto = userMapper.toDTO(user);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Primo", dto.getName());
        assertEquals("primo@example.com", dto.getEmail());
        assertEquals(Collections.singleton(Role.ROLE_CUSTOMER), dto.getRoles());
    }

    @Test
    void testToDTO_NullUser() {
        // Act
        UserResponseDTO dto = userMapper.toDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void testToEntity_NullDto() {
        // Act
        User entity = userMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }
}
