package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Page<User> getAllUsers(Pageable pageable);
}
