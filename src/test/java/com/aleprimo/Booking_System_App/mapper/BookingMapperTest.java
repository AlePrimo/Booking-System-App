package com.aleprimo.Booking_System_App.mapper;

import com.aleprimo.Booking_System_App.dto.booking.BookingRequestDTO;
import com.aleprimo.Booking_System_App.dto.booking.BookingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import com.aleprimo.Booking_System_App.mapper.booking.BookingMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper mapper = new BookingMapper();

    @Test
    void testToEntity() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        BookingRequestDTO dto = BookingRequestDTO.builder()
                .bookingDateTime(now)
                .status(BookingStatus.CONFIRMED)
                .build();

        User customer = User.builder().id(1L).name("john_doe").build();
        Offering offering = Offering.builder().id(10L).name("Massage").build();

        // Act
        Booking entity = mapper.toEntity(dto, customer, offering);

        // Assert
        assertNotNull(entity);
        assertEquals(customer, entity.getCustomer());
        assertEquals(offering, entity.getOffering());
        assertEquals(now, entity.getBookingDateTime());
        assertEquals(BookingStatus.CONFIRMED, entity.getStatus());
    }

    @Test
    void testToDTO_WithCustomerAndOffering() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User customer = User.builder().id(2L).name("jane_doe").build();
        Offering offering = Offering.builder().id(20L).name("Yoga Class").build();

        Booking entity = Booking.builder()
                .id(100L)
                .customer(customer)
                .offering(offering)
                .bookingDateTime(now)
                .status(BookingStatus.PENDING)
                .build();

        // Act
        BookingResponseDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(2L, dto.getCustomerId());
        assertEquals(20L, dto.getOfferingId());
        assertEquals(now, dto.getBookingDateTime());
        assertEquals(BookingStatus.PENDING, dto.getStatus());
    }

    @Test
    void testToDTO_WithNullCustomer() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Offering offering = Offering.builder().id(30L).name("Pilates").build();

        Booking entity = Booking.builder()
                .id(200L)
                .customer(null) // <- null
                .offering(offering)
                .bookingDateTime(now)
                .status(BookingStatus.CANCELLED)
                .build();

        // Act
        BookingResponseDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(200L, dto.getId());
        assertNull(dto.getCustomerId(), "Debe ser null si customer es null");
        assertEquals(30L, dto.getOfferingId());
        assertEquals(BookingStatus.CANCELLED, dto.getStatus());
    }

    @Test
    void testToDTO_WithNullOffering() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        User customer = User.builder().id(3L).name("mike_smith").build();

        Booking entity = Booking.builder()
                .id(300L)
                .customer(customer)
                .offering(null) // <- null
                .bookingDateTime(now)
                .status(BookingStatus.CONFIRMED)
                .build();

        // Act
        BookingResponseDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(300L, dto.getId());
        assertEquals(3L, dto.getCustomerId());
        assertNull(dto.getOfferingId(), "Debe ser null si offering es null");
        assertEquals(BookingStatus.CONFIRMED, dto.getStatus());
    }
}
