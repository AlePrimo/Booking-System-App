package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterWithValidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtil.extractUsername("valid.token")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(
                new CustomUserDetails(User.builder().email("user@example.com").password("pass").build())
        );
        when(jwtUtil.validateToken("valid.token", "user@example.com")).thenReturn(true);

        filter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterWithInvalidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtUtil.extractUsername("invalid.token")).thenThrow(new RuntimeException("bad token"));

        filter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterWithoutToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }
}
