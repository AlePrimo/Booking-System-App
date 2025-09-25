package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testUserDetailsMapping() {
        User user = User.builder()
                .email("test@example.com")
                .password("pass")
                .role(Role.ROLE_ADMIN)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("test@example.com", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertTrue(details.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));


        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
