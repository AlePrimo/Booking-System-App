package com.aleprimo.Booking_System_App.persistence;

import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;

import com.aleprimo.Booking_System_App.persistence.daoImpl.BookingDAOImpl;
import com.aleprimo.Booking_System_App.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingDAOImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingDAOImpl bookingDAO;

    private Booking booking;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.PENDING)
                .bookingDateTime(LocalDateTime.now())
                .build();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testSave() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking saved = bookingDAO.save(booking);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testFindById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingDAO.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(BookingStatus.PENDING);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findAll(pageable)).thenReturn(page);

        Page<Booking> result = bookingDAO.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookingRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindByCustomerId() {
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByCustomerId(1L, pageable)).thenReturn(page);

        Page<Booking> result = bookingDAO.findByCustomerId(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookingRepository, times(1)).findByCustomerId(1L, pageable);
    }

    @Test
    void testFindByOfferingId() {
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByOfferingId(2L, pageable)).thenReturn(page);

        Page<Booking> result = bookingDAO.findByOfferingId(2L, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookingRepository, times(1)).findByOfferingId(2L, pageable);
    }

    @Test
    void testFindByStatus() {
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByStatus(BookingStatus.PENDING, pageable)).thenReturn(page);

        Page<Booking> result = bookingDAO.findByStatus(BookingStatus.PENDING, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookingRepository, times(1)).findByStatus(BookingStatus.PENDING, pageable);
    }

    @Test
    void testFindByBookingDateTimeBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        Page<Booking> page = new PageImpl<>(List.of(booking));
        when(bookingRepository.findByBookingDateTimeBetween(start, end, pageable)).thenReturn(page);

        Page<Booking> result = bookingDAO.findByBookingDateTimeBetween(start, end, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(bookingRepository, times(1)).findByBookingDateTimeBetween(start, end, pageable);
    }

    @Test
    void testDeleteById() {
        doNothing().when(bookingRepository).deleteById(1L);

        bookingDAO.deleteById(1L);

        verify(bookingRepository, times(1)).deleteById(1L);
    }
}
