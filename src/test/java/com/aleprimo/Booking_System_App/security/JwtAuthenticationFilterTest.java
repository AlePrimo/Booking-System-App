package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

        Set<Role> roles = new HashSet<>(Collections.singleton(Role.CUSTOMER));
        User user = User.builder()
                .email("user@example.com")
                .password("pass")
                .roles(roles)
                .build();

        when(userDetailsService.loadUserByUsername("user@example.com"))
                .thenReturn(new CustomUserDetails(user));

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
        when(jwtUtil.extractUsername("invalid.token"))
                .thenThrow(new RuntimeException("bad token"));

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

    @Test
    void testDoFilterWithInvalidHeaderFormat() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);


        when(request.getHeader("Authorization")).thenReturn("Token xyz123");

        filter.doFilterInternal(request, response, chain);


        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void testDoFilterWithExistingAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtil.extractUsername("valid.token")).thenReturn("user@example.com");


        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        "alreadyAuth", null, Collections.emptyList()
                )
        );

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterWithInvalidValidation() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer some.token");
        when(jwtUtil.extractUsername("some.token")).thenReturn("user@example.com");

        Set<Role> roles = new HashSet<>(Collections.singleton(Role.CUSTOMER));
        User user = User.builder()
                .email("user@example.com")
                .password("pass")
                .roles(roles)
                .build();

        when(userDetailsService.loadUserByUsername("user@example.com"))
                .thenReturn(new CustomUserDetails(user));

        when(jwtUtil.validateToken("some.token", "user@example.com")).thenReturn(false);

        filter.doFilterInternal(request, response, chain);


        assert (SecurityContextHolder.getContext().getAuthentication() == null);
        verify(chain, times(1)).doFilter(request, response);
    }


}
