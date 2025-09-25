package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.entity.enums.PaymentMethod;
import com.aleprimo.Booking_System_App.entity.enums.PaymentStatus;
import com.aleprimo.Booking_System_App.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferingRepository offeringRepository;

    private Booking booking;
    private Payment payment;
    private User customer;
    private Offering offering;
    private User provider;

    @BeforeEach
    void setUp() {

        customer = User.builder()
                .email("pepe@mail")
                .name("pepe")
                .password("pepe1234")
                .role(Role.ROLE_CUSTOMER)
                .build();
        userRepository.save(customer);

        provider = User.builder()
                .role(Role.ROLE_PROVIDER)
                .password("provider1234")
                .email("provider@mail")
                .name("vandalay")
                .build();
        userRepository.save(provider);

        offering = Offering.builder()
                .price(BigDecimal.valueOf(100.0))
                .durationMinutes(60)
                .name("plomeria")
                .provider(provider)
                .description("Destapamiento de caconia")
                .build();

        offeringRepository.save(offering);

        booking = Booking.builder()
                .customer(customer)
                .offering(offering)
                .status(BookingStatus.PENDING)
                .bookingDateTime(LocalDateTime.now().plusDays(3))
                .build();
        booking = bookingRepository.save(booking);

        payment = Payment.builder()
                .amount(BigDecimal.valueOf(2500))
                .method(PaymentMethod.CASH)
                .status(PaymentStatus.PENDING)
                .booking(booking)
                .build();
        payment = paymentRepository.save(payment);
    }

    @Test
    void testFindById() {
        Optional<Payment> found = paymentRepository.findById(payment.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAmount()).isEqualTo(BigDecimal.valueOf(2500));
    }

    @Test
    void testFindByBookingId() {
        Optional<Payment> found = paymentRepository.findByBookingId(booking.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getBooking().getId()).isEqualTo(booking.getId());
    }

    @Test
    void testDeleteById() {
        paymentRepository.deleteById(payment.getId());
        Optional<Payment> found = paymentRepository.findById(payment.getId());
        assertThat(found).isNotPresent();
    }
}
