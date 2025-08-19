package com.aleprimo.Booking_System_App.mapper.booking;


import com.aleprimo.Booking_System_App.dto.booking.BookingRequestDTO;
import com.aleprimo.Booking_System_App.dto.booking.BookingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Booking;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequestDTO dto, User customer, Offering offering) {
        return Booking.builder()
                .customer(customer)
                .offering(offering)
                .bookingDateTime(dto.getBookingDateTime())
                .status(dto.getStatus())
                .build();
    }

    public  BookingResponseDTO toDTO(Booking entity) {
        return BookingResponseDTO.builder()
                .id(entity.getId())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null)
                .offeringId(entity.getOffering() != null ? entity.getOffering().getId() : null)
                .bookingDateTime(entity.getBookingDateTime())
                .status(entity.getStatus())
                .build();
    }
}
