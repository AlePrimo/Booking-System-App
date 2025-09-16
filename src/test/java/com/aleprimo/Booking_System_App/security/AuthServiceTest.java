package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.dto.auth.RegisterRequestDTO;
import com.aleprimo.Booking_System_App.dto.auth.RegisterResponseDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginRequestDTO;

import com.aleprimo.Booking_System_App.dto.login.LoginResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.repository.UserRepository;
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
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;
    private UserDetails userDetails;
    private LoginRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .roles(Set.of(Role.CUSTOMER))
                .build();

        userDetails = new CustomUserDetails(user);

        validRequest = new LoginRequestDTO("john@example.com", "password");
    }

    @Test
    void login_returnsAccessAndRefreshToken_whenCredentialsAreValid() {
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn("mocked-access");
        when(jwtUtil.generateRefreshToken(userDetails.getUsername())).thenReturn("mocked-refresh");

        LoginResponseDTO response = authService.login(validRequest);

        assertThat(response.getToken()).isEqualTo("mocked-access");
        assertThat(response.getRefreshToken()).isEqualTo("mocked-refresh");

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_throwsException_whenUserDetailsAreNull() {
        when(userDetailsService.loadUserByUsername(validRequest.getEmail())).thenReturn(null);

        assertThatThrownBy(() -> authService.login(validRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email o contraseña incorrectos");
    }

    @Test
    void register_savesNewUser() {
        RegisterRequestDTO request = new RegisterRequestDTO("John", "john@example.com", "password123");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        RegisterResponseDTO response = authService.register(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void refresh_returnsNewAccessToken_whenValid() {
        String refreshToken = "valid-refresh";
        when(jwtUtil.extractUsername(refreshToken)).thenReturn("john@example.com");
        when(jwtUtil.isTokenExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.generateToken("john@example.com")).thenReturn("new-access");

        LoginResponseDTO response = authService.refresh(refreshToken);

        assertThat(response.getToken()).isEqualTo("new-access");
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    }

}
