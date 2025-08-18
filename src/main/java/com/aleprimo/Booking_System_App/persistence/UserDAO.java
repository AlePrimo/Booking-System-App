package com.aleprimo.Booking_System_App.persistence;



import com.aleprimo.Booking_System_App.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
}
