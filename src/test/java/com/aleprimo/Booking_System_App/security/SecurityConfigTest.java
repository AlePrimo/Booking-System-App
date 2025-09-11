package com.aleprimo.Booking_System_App.security;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;


import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@SpringBootTest
class SecurityConfigTest {
    private JwtAuthenticationFilter jwtFilter;
    private CustomUserDetailsService userDetailsService;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtFilter = mock(JwtAuthenticationFilter.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        securityConfig = new SecurityConfig(userDetailsService, jwtFilter);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isNotNull();
        String encoded = encoder.encode("test");
        assertThat(encoded).isNotEqualTo("test");
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        AuthenticationManager result = securityConfig.authenticationManager(authConfig);
        assertThat(result).isEqualTo(authManager);
    }

    @Test
    void testJwtFilterInjection() {
        assertThat(securityConfig).isNotNull();
        assertThat(securityConfig).hasFieldOrProperty("jwtFilter");
    }

    @Test
    void testUserDetailsServiceInjection() {
        assertThat(securityConfig).isNotNull();
        assertThat(securityConfig).hasFieldOrProperty("userDetailsService");
    }

    // NOTA: SecurityFilterChain depende de HttpSecurity y no se puede instanciar en unit test
    // Podés testear que la clase tiene el método correcto, pero no ejecutarlo:
    @Test
    void testSecurityFilterChainMethodExists() throws NoSuchMethodException {
        assertThat(SecurityConfig.class.getMethod("securityFilterChain", org.springframework.security.config.annotation.web.builders.HttpSecurity.class))
                .isNotNull();
    }
//    @Autowired
//    private ApplicationContext context;
//
//    @Autowired
//    private SecurityFilterChain securityFilterChain;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtAuthenticationFilter jwtFilter;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Test
//    void testSecurityFilterChainBeanExists() {
//        assertThat(securityFilterChain).isNotNull();
//    }
//
//    @Test
//    void testSecurityFilterChainContainsJwtFilter() throws Exception {
//        // Verifica que el JwtAuthenticationFilter esté en la cadena de filtros
//        boolean containsJwtFilter = securityFilterChain.getFilters()
//                .stream()
//                .anyMatch(f -> f.getClass().equals(JwtAuthenticationFilter.class));
//        assertThat(containsJwtFilter).isTrue();
//    }
//
//    @Test
//    void testPasswordEncoderBeanExists() {
//        assertThat(passwordEncoder).isNotNull();
//        // Opcional: probar encode
//        String encoded = passwordEncoder.encode("testPassword");
//        assertThat(encoded).isNotEmpty();
//        assertThat(encoded).isNotEqualTo("testPassword");
//    }
//
//    @Test
//    void testAuthenticationManagerBeanExists() {
//        assertThat(authenticationManager).isNotNull();
//    }
//
//    @Test
//    void testJwtAuthenticationFilterBeanExists() {
//        assertThat(jwtFilter).isNotNull();
//    }
//
//    @Test
//    void testCustomUserDetailsServiceBeanExists() {
//        assertThat(userDetailsService).isNotNull();
//    }
//
//    @Test
//    void testHttpSecurityCanBeBuilt() throws Exception {
//        // Verifica que SecurityFilterChain pueda construirse con un HttpSecurity real
//        SecurityFilterChain http = context.getBean(SecurityConfig.class).securityFilterChain(context.getBean(HttpSecurity.class));
//        assertThat(http).isNotNull();
//    }

}
