package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
@RequiredArgsConstructor
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = User.builder()
                .name("Juan Pérez")
                .email("juan@mail.com")
                .password("123456")
                .role(Role.ROLE_CUSTOMER)
                .build();

        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("juan@mail.com");
    }

    @Test
    void testFindByEmail() {
        User user = User.builder()
                .name("Ana López")
                .email("ana@mail.com")
                .password("abcdef")
                .role(Role.ROLE_PROVIDER)
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("ana@mail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Ana López");
    }

    @Test
    void testExistsByEmail() {
        User user = User.builder()
                .name("Carlos")
                .email("carlos@mail.com")
                .password("qwerty")
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("carlos@mail.com");

        assertThat(exists).isTrue();
    }

    @Test
    void testDeleteById() {
        User user = User.builder()
                .name("Eliminar")
                .email("delete@mail.com")
                .password("delete")
                .role(Role.ROLE_CUSTOMER)
                .build();

        User saved = userRepository.save(user);
        userRepository.deleteById(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isEmpty();
    }
}
