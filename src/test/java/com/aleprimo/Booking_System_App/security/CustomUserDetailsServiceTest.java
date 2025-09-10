package com.aleprimo.Booking_System_App.security;

import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CustomUserDetailsService service = new CustomUserDetailsService(userRepository);

    @Test
    void testLoadUserByUsernameFound() {
        User user = User.builder().email("test@example.com").password("pass").build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("test@example.com");
        assertEquals("test@example.com", details.getUsername());
        assertEquals("pass", details.getPassword());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com"));
    }
}
