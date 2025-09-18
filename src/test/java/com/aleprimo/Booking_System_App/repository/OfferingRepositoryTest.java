package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OfferingRepositoryTest {

    @Autowired
    private OfferingRepository offeringRepository;
    @Autowired
    private UserRepository userRepository;

    private Offering offering;
    private User provider;

    @BeforeEach
    void setUp() {

        provider = User.builder()
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .roles(Set.of(Role.ROLE_PROVIDER))
                .build();
userRepository.save(provider);
        offering = Offering.builder()
                .name("Test Offering")
                .description("Desc")
                .price(BigDecimal.valueOf(100.0))
                .durationMinutes(15)
                .provider(provider)
                .build();
        offeringRepository.save(offering);
    }

    @Test
    void testSaveAndFindById() {
        Offering found = offeringRepository.findById(offering.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Offering");
    }

    @Test
    void testFindAll() {
        List<Offering> list = offeringRepository.findAll();
        assertThat(list).isNotEmpty();
    }

    @Test
    void testDelete() {
        offeringRepository.delete(offering);
        assertThat(offeringRepository.findById(offering.getId())).isEmpty();
    }
}
