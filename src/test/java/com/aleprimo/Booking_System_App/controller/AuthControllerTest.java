package com.aleprimo.Booking_System_App.controller;


import com.aleprimo.Booking_System_App.controller.authorization.AuthController;
import com.aleprimo.Booking_System_App.dto.auth.RegisterRequestDTO;
import com.aleprimo.Booking_System_App.dto.auth.RegisterResponseDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginRequestDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginResponseDTO;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.security.AuthService;
import com.aleprimo.Booking_System_App.security.CustomUserDetailsService;
import com.aleprimo.Booking_System_App.security.JwtUtil;
import com.aleprimo.Booking_System_App.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)

class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void login_returnsAccessAndRefreshToken() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user@example.com", "pass");
        LoginResponseDTO response = new LoginResponseDTO("jwt-access", "jwt-refresh", Role.ROLE_CUSTOMER);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-access"))
                .andExpect(jsonPath("$.refreshToken").value("jwt-refresh"));
    }

    @Test
    void register_returnsUserData() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "John",
                "john@example.com",
                "password123",
                Role.ROLE_CUSTOMER
        );
        RegisterResponseDTO response = new RegisterResponseDTO(1L, "John", "john@example.com");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
               
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))

                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    @Test
    void refresh_returnsNewAccessToken() throws Exception {
        String refreshToken = "jwt-refresh";
        LoginResponseDTO response = new LoginResponseDTO("jwt-new-access", refreshToken, Role.ROLE_CUSTOMER);

        when(authService.refresh(any(String.class))).thenReturn(response);

        String body = """
        { "refreshToken": "%s" }
        """.formatted(refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-new-access"))
                .andExpect(jsonPath("$.refreshToken").value("jwt-refresh"));
    }

}
