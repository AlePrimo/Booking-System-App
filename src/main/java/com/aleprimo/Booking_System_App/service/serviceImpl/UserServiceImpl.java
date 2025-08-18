package com.aleprimo.Booking_System_App.service.serviceImpl;


import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.persistence.UserDAO;
import com.aleprimo.Booking_System_App.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    @Operation(summary = "Crear un nuevo usuario")
    public User createUser(User user) {
        return userDAO.save(user);
    }

    @Override
    @Operation(summary = "Actualizar un usuario existente")
    public User updateUser(Long id, User user) {
        User existing = userDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        existing.setRoles(user.getRoles());
        return userDAO.save(existing);
    }

    @Override
    @Operation(summary = "Eliminar un usuario por ID")
    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    @Operation(summary = "Obtener un usuario por ID")
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    @Operation(summary = "Obtener un usuario por email")
    public Optional<User> getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    @Operation(summary = "Obtener todos los usuarios con paginación")
    public Page<User> getAllUsers(Pageable pageable) {
        List<User> users = userDAO.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), users.size());
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }
}
