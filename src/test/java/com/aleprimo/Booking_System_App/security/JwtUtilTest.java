package com.aleprimo.Booking_System_App.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "7LnkNHuqRh55wWY3hYDDLdirV//5jyqMsDAQZQo0NroS325C6ue+B+jC+pJJxKCYd/G2gfZyeKbe9w73ZtZW+Q==");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 50L);
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);
        assertNotNull(token);

        String extractedEmail = jwtUtil.extractUsername(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testValidateTokenSuccessAndFailure() throws InterruptedException {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // token válido
        assertTrue(jwtUtil.validateToken(token, email));

        // token con email distinto
        assertFalse(jwtUtil.validateToken(token, "other@example.com"));

        // token expirado
        Thread.sleep(60);
        assertFalse(jwtUtil.validateToken(token, email));
    }

    @Test
    void testExtractClaimExpirationAndIsExpired() throws InterruptedException {
        String token = jwtUtil.generateToken("user@example.com");
        Date expiration = jwtUtil.extractAllClaims(token).getExpiration();
        assertNotNull(expiration);

        // esperar a que expire
        Thread.sleep(60);
        assertTrue(jwtUtil.extractAllClaims(token).getExpiration().before(new Date()));
    }

    @Test
    void testMalformedTokenThrowsException() {
        String badToken = "malformed.token";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(badToken));
    }
}
