package com.aleprimo.Booking_System_App.persistence;

import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.persistence.daoImpl.PaymentDAOImpl;
import com.aleprimo.Booking_System_App.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentDAOImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentDAOImpl paymentDAO;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
    }

    @Test
    void save_ShouldReturnSavedPayment() {
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentDAO.save(payment);

        assertThat(result).isEqualTo(payment);
    }

    @Test
    void findById_ShouldReturnPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentDAO.findById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findByBookingId_ShouldReturnPayment() {
        when(paymentRepository.findByBookingId(5L)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentDAO.findByBookingId(5L);

        assertThat(result).isPresent();
    }

    @Test
    void findAll_ShouldReturnPageOfPayments() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Payment> page = new PageImpl<>(List.of(payment));
        when(paymentRepository.findAll(pageable)).thenReturn(page);

        Page<Payment> result = paymentDAO.findAll(pageable);

        assertThat(result.getContent()).contains(payment);
    }

    @Test
    void deleteById_ShouldInvokeRepository() {
        paymentDAO.deleteById(1L);
        verify(paymentRepository, times(1)).deleteById(1L);
    }
}
