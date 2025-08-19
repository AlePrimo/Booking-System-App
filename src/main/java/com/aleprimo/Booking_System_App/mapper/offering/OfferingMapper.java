package com.aleprimo.Booking_System_App.mapper.offering;


import com.aleprimo.Booking_System_App.dto.offering.OfferingRequestDTO;
import com.aleprimo.Booking_System_App.dto.offering.OfferingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Offering;
import org.springframework.stereotype.Component;

@Component
public class OfferingMapper {

    public  Offering toEntity(OfferingRequestDTO dto) {
        return Offering.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .durationMinutes(dto.getDurationMinutes())
                .price(dto.getPrice())
                .build();
    }

    public  OfferingResponseDTO toDTO(Offering entity) {
        return OfferingResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .durationMinutes(entity.getDurationMinutes())
                .price(entity.getPrice())
                .providerId(entity.getProvider() != null ? entity.getProvider().getId() : null)
                .build();
    }
}
