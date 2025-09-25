package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;

import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;



import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferingRepository offeringRepository;

    private User customer;
    private Offering offering;
    private Booking booking;
    private User provider;

    @BeforeEach
    void setUp() {

        provider = User.builder()
                .name("VANDALAY")
                .email("vandalay@mail")
                .password("vandalay124")
                .role(Role.ROLE_PROVIDER)
                .build();
        userRepository.save(provider);

        customer = User.builder()
                .name("Juan")
                .email("juan@mail.com")
                .password("1234")
                .role(Role.ROLE_CUSTOMER)
                .build();
        userRepository.save(customer);

        offering = Offering.builder()
                .name("Servicio Test")
                .description("desc")
                .price(BigDecimal.valueOf(300.0))
                .durationMinutes(15)
                .provider(provider)
                .build();
        offeringRepository.save(offering);

        booking = Booking.builder()
                .bookingDateTime(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.PENDING)
                .customer(customer)
                .offering(offering)
                .build();
        bookingRepository.save(booking);
    }

    @Test
    void testFindByCustomerId() {
        Page<Booking> page = bookingRepository.findByCustomerId(customer.getId(), PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    void testFindByOfferingId() {
        Page<Booking> page = bookingRepository.findByOfferingId(offering.getId(), PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    void testFindByStatus() {
        Page<Booking> page = bookingRepository.findByStatus(BookingStatus.PENDING, PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }
}