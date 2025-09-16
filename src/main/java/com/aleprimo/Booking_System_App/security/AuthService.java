package com.aleprimo.Booking_System_App.security;


import com.aleprimo.Booking_System_App.dto.login.LoginRequestDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            // Autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Generar token JWT
            final var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            final String token = jwtUtil.generateToken(userDetails.getUsername());

            return LoginResponseDTO.builder()
                    .token(token)
                    .build();

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }
    }
}
