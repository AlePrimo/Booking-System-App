package com.aleprimo.Booking_System_App.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "7LnkNHuqRh55wWY3hYDDLdirV//5jyqMsDAQZQo0NroS325C6ue+B+jC+pJJxKCYd/G2gfZyeKbe9w73ZtZW+Q==");
        // 👉 tokens "largos" para los tests normales
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 3600000L); // 1 hora
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
    void testValidateTokenSuccessAndFailure() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        // válido
        assertTrue(jwtUtil.validateToken(token, email));

        // email distinto
        assertFalse(jwtUtil.validateToken(token, "other@example.com"));
    }

    @Test
    void generateRefreshToken_producesValidToken_andClaimsAreCorrect() {
        JwtUtil jwtUtil = new JwtUtil();

        // generar secret seguro base64 estándar
        byte[] random = new byte[64];
        new SecureRandom().nextBytes(random);
        String secret = Base64.getEncoder().encodeToString(random); // <--- aquí

        // inyectar valores privados
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 1000L);

        String email = "user@example.com";
        String token = jwtUtil.generateRefreshToken(email);

        assertThat(token).isNotNull();

        Claims claims = jwtUtil.extractAllClaims(token);

        assertThat(claims.getSubject()).isEqualTo(email);

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        assertThat(issuedAt).isNotNull();
        assertThat(expiration).isNotNull();
        assertThat(expiration.after(issuedAt)).isTrue();
    }
    @Test
    void testValidateToken_expired_usingSpy() throws Exception {
        String email = "test@example.com";
        // generás un token válido con tu método existente
        String token = jwtUtil.generateToken(email);

        // spy sobre jwtUtil (si jwtUtil es @Autowired en tu test, no lo replico aquí)
        JwtUtil spyJwt = spy(jwtUtil);

        // forzamos que extractUsername devuelva el email correcto
        doReturn(email).when(spyJwt).extractUsername(token);

        // forzamos que isTokenExpired devuelva true (token vencido)
        doReturn(true).when(spyJwt).isTokenExpired(token);

        // ahora validateToken debe dar false (porque está vencido)
        assertFalse(spyJwt.validateToken(token, email));
    }

    @Test
    void testExtractClaimExpirationAndIsExpired() {
        // 👉 uso un JwtUtil con expiración corta
        JwtUtil shortLivedJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortLivedJwtUtil, "secret",
                "7LnkNHuqRh55wWY3hYDDLdirV//5jyqMsDAQZQo0NroS325C6ue+B+jC+pJJxKCYd/G2gfZyeKbe9w73ZtZW+Q==");
        ReflectionTestUtils.setField(shortLivedJwtUtil, "jwtExpirationMs", 1L);

        String token = shortLivedJwtUtil.generateToken("user@example.com");

        // inmediatamente debe estar expirado
        assertThrows(ExpiredJwtException.class, () -> shortLivedJwtUtil.extractAllClaims(token));
    }

    @Test
    void testMalformedTokenThrowsException() {
        String badToken = "malformed.token";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(badToken));
    }
}
