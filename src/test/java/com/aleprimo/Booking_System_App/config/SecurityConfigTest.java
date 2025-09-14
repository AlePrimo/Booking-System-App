package com.aleprimo.Booking_System_App.config;

import com.aleprimo.Booking_System_App.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfigTest.DummyController.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    // --- SECURITY FILTER CHAIN ---

    @Test
    void authEndpoints_arePermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk()); // permitido sin auth
    }

    @Test
    void protectedEndpoints_requireAuthentication() throws Exception {
        mockMvc.perform(get("/protected"))
                .andExpect(status().isForbidden()); // requiere auth
    }

    @Test
    @WithMockUser
    void protectedEndpoints_allowAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/protected"))
                .andExpect(status().isOk()); // autenticado -> OK
    }

    // --- PASSWORD ENCODER ---

    @Test
    void passwordEncoder_isBCrypt_andMatches() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        String raw = "mySecret";
        String encoded = encoder.encode(raw);

        assertThat(encoded).isNotEqualTo(raw);
        assertThat(encoder.matches(raw, encoded)).isTrue();
    }

    // --- AUTHENTICATION MANAGER ---
    @Test
    void authenticationManager_isNotNull() throws Exception {
        AuthenticationManager manager = securityConfig.authenticationManager(authenticationConfiguration);
        assertThat(manager).isNotNull();
    }

//    @Test
//    void authenticationManager_returnsFromConfiguration() throws Exception {
//        AuthenticationManager managerMock = mock(AuthenticationManager.class);
//        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(managerMock);
//
//        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);
//
//        assertThat(result).isSameAs(managerMock);
//    }

    // --- DUMMY CONTROLLER PARA LOS TESTS ---
    @RestController
    static class DummyController {
        @GetMapping("/auth/login")
        public String login() {
            return "login ok";
        }

        @GetMapping("/protected")
        public String protectedEndpoint() {
            return "protected ok";
        }
    }
}