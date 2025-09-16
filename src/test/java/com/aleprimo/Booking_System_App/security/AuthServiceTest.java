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


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;



import java.util.Optional;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;
    private UserDetails userDetails;
    private LoginRequestDTO validLogin;
    private RegisterRequestDTO validRegister;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .roles(Set.of(Role.CUSTOMER))
                .build();

        userDetails = new CustomUserDetails(user);

        validLogin = new LoginRequestDTO("john@example.com", "password");
        validRegister = new RegisterRequestDTO("John Doe", "john@example.com", "password123");

        // Mocks lenientes para evitar UnnecessaryStubbingException
        lenient().when(userDetailsService.loadUserByUsername(validLogin.getEmail())).thenReturn(userDetails);
        lenient().when(jwtUtil.generateToken(any())).thenReturn("mocked-token");
        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        lenient().when(userRepository.save(any(User.class))).thenReturn(user);
    }

    // ===== LOGIN =====
    @Test
    void login_returnsToken_whenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        LoginResponseDTO response = authService.login(validLogin);

        assertThat(response.getToken()).isEqualTo("mocked-token");
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(userDetails.getUsername());
    }

    @Test
    void login_throwsException_whenUserDetailsAreNull() {
        when(userDetailsService.loadUserByUsername(validLogin.getEmail())).thenReturn(null);

        assertThatThrownBy(() -> authService.login(validLogin))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email o contraseña incorrectos");
    }

    @Test
    void login_throwsException_whenAuthenticationFails() {
        doThrow(new BadCredentialsException("Email o contraseña incorrectos"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThatThrownBy(() -> authService.login(validLogin))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Email o contraseña incorrectos");
    }

    // ===== REGISTER =====
    @Test
    void register_returnsResponse_whenDataIsValid() {
        RegisterResponseDTO response = authService.register(validRegister);

        assertThat(response.getEmail()).isEqualTo(validRegister.getEmail());
        assertThat(response.getName()).isEqualTo(validRegister.getName());
        assertThat(response.getId()).isEqualTo(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_throwsException_whenEmailAlreadyUsed() {
        when(userRepository.findByEmail(validRegister.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.register(validRegister))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya está en uso");
    }

    @Test
    void register_throwsException_whenPasswordTooShort() {
        RegisterRequestDTO shortPass = new RegisterRequestDTO("Jane", "jane@example.com", "123");

        assertThatThrownBy(() -> authService.register(shortPass))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La contraseña debe tener al menos 6 caracteres");
    }

    // ===== REFRESH TOKEN =====
    @Test
    void refresh_returnsNewAccessToken_whenValid() {
        String refreshToken = "valid-refresh";
        when(jwtUtil.extractUsername(refreshToken)).thenReturn("john@example.com");
        when(jwtUtil.isTokenExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.generateToken("john@example.com")).thenReturn("new-access-token");

        LoginResponseDTO response = authService.refresh(refreshToken);

        assertThat(response.getToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    void refresh_throwsBadCredentials_whenExtractUsernameFails() {
        String refreshToken = "invalid-refresh";
        when(jwtUtil.extractUsername(refreshToken)).thenThrow(new RuntimeException("fail"));

        assertThatThrownBy(() -> authService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Refresh token inválido");
    }

    @Test
    void refresh_throwsBadCredentials_whenTokenExpired() {
        String refreshToken = "expired-refresh";
        when(jwtUtil.extractUsername(refreshToken)).thenReturn("john@example.com");
        when(jwtUtil.isTokenExpired(refreshToken)).thenReturn(true);

        assertThatThrownBy(() -> authService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Refresh token expirado");
    }



}
