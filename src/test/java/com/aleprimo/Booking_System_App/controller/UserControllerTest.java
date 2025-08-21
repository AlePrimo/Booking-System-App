package com.aleprimo.Booking_System_App.controller;

import com.aleprimo.Booking_System_App.controller.user.UserController;
import com.aleprimo.Booking_System_App.dto.user.UserRequestDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.mapper.user.UserMapper;
import com.aleprimo.Booking_System_App.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@mail.com")
                .password("123456")
                .roles(Set.of(Role.CUSTOMER))
                .build();

        userRequestDTO = UserRequestDTO.builder()
                .name("Juan Pérez")
                .email("juan@mail.com")
                .password("123456")
                .roles(Set.of(Role.CUSTOMER))
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userMapper.toDTO(user));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userMapper.toDTO(user));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userMapper.toDTO(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDTO(any(User.class))).thenReturn(userMapper.toDTO(user));

        mockMvc.perform(get("/api/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("juan@mail.com"));
    }
}
