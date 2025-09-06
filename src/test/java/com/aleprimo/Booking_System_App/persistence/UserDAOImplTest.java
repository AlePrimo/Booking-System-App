package com.aleprimo.Booking_System_App.persistence;

import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.persistence.daoImpl.UserDAOImpl;
import com.aleprimo.Booking_System_App.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDAOImpl userDAO;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void save_ShouldReturnSavedUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userDAO.save(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userDAO.findById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userDAO.findByEmail("test@example.com");

        assertThat(result).isPresent();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userDAO.findAll();

        assertThat(result).contains(user);
    }

    @Test
    void deleteById_ShouldInvokeRepository() {
        userDAO.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
