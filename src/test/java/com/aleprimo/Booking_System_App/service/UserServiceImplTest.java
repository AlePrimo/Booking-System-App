package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import com.aleprimo.Booking_System_App.persistence.UserDAO;
import com.aleprimo.Booking_System_App.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@mail.com")
                .password("123456")
                .roles(Set.of(Role.CUSTOMER))
                .build();
    }

    @Test
    void testCreateUser() {
        when(userDAO.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan@mail.com");
        verify(userDAO, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));
        when(userDAO.save(any(User.class))).thenReturn(user);

        user.setName("Nuevo Nombre");
        User result = userService.updateUser(1L, user);

        assertThat(result.getName()).isEqualTo("Nuevo Nombre");
        verify(userDAO).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(1L, user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userDAO).deleteById(1L);

        userService.deleteUser(1L);

        verify(userDAO, times(1)).deleteById(1L);
    }

    @Test
    void testGetUserById() {
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("juan@mail.com");
    }

    @Test
    void testGetUserByEmail() {
        when(userDAO.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("juan@mail.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Juan Pérez");
    }

    @Test
    void testGetAllUsers() {
        when(userDAO.findAll()).thenReturn(List.of(user));

        Page<User> result = userService.getAllUsers(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("juan@mail.com");
    }
}
