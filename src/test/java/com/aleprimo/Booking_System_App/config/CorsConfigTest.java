package com.aleprimo.Booking_System_App.config;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.Mockito.*;

class CorsConfigTest {
    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void testCorsConfigurer_addsCorsMappings() {
        // mock del registry y del objeto que devuelve addMapping
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        // when: addMapping(...) devuelve el CorsRegistration
        when(registry.addMapping(anyString())).thenReturn(registration);

        // stubs para los métodos encadenados (varargs se stubean con any(String[].class))
        when(registration.allowedOrigins(anyString())).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);

        // obtener el WebMvcConfigurer y ejecutar
        WebMvcConfigurer configurer = corsConfig.corsConfigurer();
        configurer.addCorsMappings(registry);

        // verificaciones: addMapping en registry y luego llamadas en registration
        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("http://localhost:5173");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
    }
}
