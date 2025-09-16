package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.dto.login.LoginRequestDTO;

import com.aleprimo.Booking_System_App.dto.login.LoginResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private UserDetails userDetails;
    private LoginRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .roles(Set.of())
                .build();

        userDetails = new CustomUserDetails(user);

        validRequest = new LoginRequestDTO();
        validRequest.setEmail("john@example.com");
        validRequest.setPassword("password");
    }

    @Test
    void login_returnsToken_whenCredentialsAreValid() {
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn("mocked-token");

        LoginResponseDTO response = authService.login(validRequest);

        assertThat(response.getToken()).isEqualTo("mocked-token");

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(userDetails.getUsername());
    }

    @Test
    void login_throwsException_whenUserDetailsAreNull() {
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(null);

        assertThatThrownBy(() -> authService.login(validRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email o contraseña incorrectos");
    }

    @Test
    void login_throwsException_whenAuthenticationFails() {
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(userDetails);
        doThrow(new BadCredentialsException("Email o contraseña incorrectos"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(validRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email o contraseña incorrectos");
    }

}
