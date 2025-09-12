package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.security.CustomUserDetailsService;
import com.aleprimo.Booking_System_App.security.JwtAuthenticationFilter;
import com.aleprimo.Booking_System_App.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtAuthenticationFilter jwtFilter;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(userDetailsService, jwtFilter);
    }

    @Test
    void securityFilterChain_configures_all_parts_and_adds_filter_before_build() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);

        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any(Class.class))).thenReturn(http);

        // devolvemos un DefaultSecurityFilterChain real, no un mock
        DefaultSecurityFilterChain realChain = new DefaultSecurityFilterChain(null, Collections.emptyList());
        when(http.build()).thenReturn(realChain);

        SecurityFilterChain chain = securityConfig.securityFilterChain(http);

        assertThat(chain).isSameAs(realChain);

        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).addFilterBefore(eq(jwtFilter), eq(UsernamePasswordAuthenticationFilter.class));
        verify(http).build();
    }

    @Test
    void passwordEncoder_is_bcrypt_and_matches_password() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isNotNull();

        String raw = "miPasswordSecreta";
        String encoded = encoder.encode(raw);

        assertThat(encoded).isNotEqualTo(raw);
        assertThat(encoder.matches(raw, encoded)).isTrue();
    }

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void authenticationManager_returns_value_from_authenticationConfiguration() throws Exception {
        AuthenticationManager managerMock = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(managerMock);

        AuthenticationManager manager = securityConfig.authenticationManager(authenticationConfiguration);

        assertThat(manager).isSameAs(managerMock);
    }
}
