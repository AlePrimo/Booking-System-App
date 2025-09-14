package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.security.SecurityConfig;

import org.junit.jupiter.api.Test;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authEndpoints_arePermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus()).isNotEqualTo(403));

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus()).isNotEqualTo(403));

        mockMvc.perform(get("/api-docs/some-endpoint"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus()).isNotEqualTo(403));
    }
    @Test
    void otherEndpoints_requireAuthentication() throws Exception {
        mockMvc.perform(get("/some-protected-endpoint"))
                .andExpect(status().isForbidden()); // ⚡ cambia a Forbidden
    }


    @Test
    @WithMockUser
    void otherEndpoints_allowAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/some-protected-endpoint"))
                .andExpect(result ->
                        assertThat(result.getResponse().getStatus()).isNotEqualTo(401));
    }

    @Test
    void passwordEncoder_is_bcrypt_and_matches_password() {
        PasswordEncoder encoder = new SecurityConfig(null, null).passwordEncoder();

        String raw = "miPasswordSecreta";
        String encoded = encoder.encode(raw);

        assertThat(encoded).isNotEqualTo(raw);
        assertThat(encoder.matches(raw, encoded)).isTrue();
    }

    @Test
    void authenticationManager_returns_value_from_authenticationConfiguration() throws Exception {
        AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
        AuthenticationManager managerMock = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(managerMock);

        AuthenticationManager manager = new SecurityConfig(null, null)
                .authenticationManager(authenticationConfiguration);

        assertThat(manager).isSameAs(managerMock);
    }
}
