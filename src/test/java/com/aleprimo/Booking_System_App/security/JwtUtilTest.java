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


        assertTrue(jwtUtil.validateToken(token, email));


        assertFalse(jwtUtil.validateToken(token, "other@example.com"));
    }

    @Test
    void generateRefreshToken_producesValidToken_andClaimsAreCorrect() {
        JwtUtil jwtUtil = new JwtUtil();


        byte[] random = new byte[64];
        new SecureRandom().nextBytes(random);
        String secret = Base64.getEncoder().encodeToString(random); // <--- aquí


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

        String token = jwtUtil.generateToken(email);


        JwtUtil spyJwt = spy(jwtUtil);


        doReturn(email).when(spyJwt).extractUsername(token);


        doReturn(true).when(spyJwt).isTokenExpired(token);


        assertFalse(spyJwt.validateToken(token, email));
    }

    @Test
    void testExtractClaimExpirationAndIsExpired() {

        JwtUtil shortLivedJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortLivedJwtUtil, "secret",
                "7LnkNHuqRh55wWY3hYDDLdirV//5jyqMsDAQZQo0NroS325C6ue+B+jC+pJJxKCYd/G2gfZyeKbe9w73ZtZW+Q==");
        ReflectionTestUtils.setField(shortLivedJwtUtil, "jwtExpirationMs", 1L);

        String token = shortLivedJwtUtil.generateToken("user@example.com");


        assertThrows(ExpiredJwtException.class, () -> shortLivedJwtUtil.extractAllClaims(token));
    }

    @Test
    void testMalformedTokenThrowsException() {
        String badToken = "malformed.token";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(badToken));
    }
}
