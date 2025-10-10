package com.aleprimo.Booking_System_App.mapper;

import com.aleprimo.Booking_System_App.dto.payment.PaymentRequestDTO;
import com.aleprimo.Booking_System_App.dto.payment.PaymentResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Payment;
import com.aleprimo.Booking_System_App.mapper.payment.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapper();
    }

    @Test
    void testToEntity() {
        // Arrange
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .amount(BigDecimal.valueOf(150.0))
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .build();

        // Act
        Payment entity = paymentMapper.toEntity(dto, booking);

        // Assert
        assertNotNull(entity);
        assertEquals(BigDecimal.valueOf(150.0), entity.getAmount());
        assertEquals(booking, entity.getBooking());
    }

    @Test
    void testToDTO() {
        // Arrange
        Booking booking = Booking.builder()
                .id(2L)
                .build();

        Payment entity = Payment.builder()
                .id(10L)
                .amount(BigDecimal.valueOf(250.0))
                .booking(booking)
                .build();

        // Act
        PaymentResponseDTO dto = paymentMapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(BigDecimal.valueOf(250.0), dto.getAmount());
        assertEquals(2L,dto.getBookingId());
    }
    @Test
    void testToDTONullPayment() {
        // Act
        PaymentResponseDTO dto = paymentMapper.toDTO(null);

        // Assert
        assertNull(dto, "El DTO debe ser null cuando el Payment es null");
    }

    @Test
    void testToDTOWithNullBooking() {
        // Arrange
        Payment entity = Payment.builder()
                .id(11L)
                .amount(BigDecimal.valueOf(300.0))
                .booking(null) // Caso cuando no hay booking
                .build();

        // Act
        PaymentResponseDTO dto = paymentMapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(11L, dto.getId());
        assertEquals(BigDecimal.valueOf(300.0), dto.getAmount());
        assertNull(dto.getBookingId());
    }
}
