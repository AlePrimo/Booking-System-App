package com.aleprimo.Booking_System_App.security;


import com.aleprimo.Booking_System_App.dto.auth.RegisterRequestDTO;
import com.aleprimo.Booking_System_App.dto.auth.RegisterResponseDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginRequestDTO;
import com.aleprimo.Booking_System_App.dto.login.LoginResponseDTO;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());
        if (userDetails == null) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }


        String accessToken = jwtUtil.generateToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());


        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        return LoginResponseDTO.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("El email ya está en uso");
        });

        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        User saved = userRepository.save(user);

        return new RegisterResponseDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    public LoginResponseDTO refresh(String refreshToken) {
        String username;
       User user;
        try {
            username = jwtUtil.extractUsername(refreshToken);
            user = userRepository.findByEmail(username).get();


        } catch (Exception e) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new BadCredentialsException("Refresh token expirado");
        }

        String newAccessToken = jwtUtil.generateToken(username);

        return new LoginResponseDTO(newAccessToken, refreshToken, user.getRole());
    }

}
