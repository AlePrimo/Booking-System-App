package com.aleprimo.Booking_System_App.config;


import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final CorsConfig corsConfig = new CorsConfig();

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        assertNotNull(source);

        // Crear request mock
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/any/path");

        // Obtener configuración
        CorsConfiguration config = source.getCorsConfiguration(request);
        assertNotNull(config);

        // Verificaciones
        assertEquals(3, config.getAllowedOrigins().size());
        assertTrue(config.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(config.getAllowedOrigins().contains("http://192.168.137.1:5173"));
        assertTrue(config.getAllowedOrigins().contains("http://192.168.100.72:5173"));

        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedMethods().contains("PUT"));
        assertTrue(config.getAllowedMethods().contains("DELETE"));
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));

        assertTrue(config.getAllowedHeaders().contains("*"));
        assertTrue(config.getAllowCredentials());
        assertEquals(3600L, config.getMaxAge());
    }
}