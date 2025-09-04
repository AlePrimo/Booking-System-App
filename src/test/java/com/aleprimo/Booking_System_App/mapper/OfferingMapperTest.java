package com.aleprimo.Booking_System_App.mapper;

import com.aleprimo.Booking_System_App.dto.offering.OfferingRequestDTO;
import com.aleprimo.Booking_System_App.dto.offering.OfferingResponseDTO;
import com.aleprimo.Booking_System_App.entity.Offering;
import com.aleprimo.Booking_System_App.entity.User;
import com.aleprimo.Booking_System_App.mapper.offering.OfferingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OfferingMapperTest {

    private OfferingMapper offeringMapper;

    @BeforeEach
    void setUp() {
        offeringMapper = new OfferingMapper();
    }

    @Test
    void testToEntity_shouldMapFieldsCorrectly() {
        OfferingRequestDTO dto = OfferingRequestDTO.builder()
                .name("Test Offering")
                .description("Test Description")
                .durationMinutes(60)
                .price(BigDecimal.valueOf(100.0))
                .build();

        User provider = User.builder()
                .id(1L)
                .name("providerUser")
                .build();

        Offering entity = offeringMapper.toEntity(dto, provider);

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Test Offering");
        assertThat(entity.getDescription()).isEqualTo("Test Description");
        assertThat(entity.getDurationMinutes()).isEqualTo(60);
        assertThat(entity.getPrice()).isEqualTo(BigDecimal.valueOf(100.0));
        assertThat(entity.getProvider()).isEqualTo(provider);
    }

    @Test
    void testToDTO_shouldMapFieldsCorrectly() {
        User provider = User.builder().id(2L).build();

        Offering entity = Offering.builder()
                .id(10L)
                .name("Entity Offering")
                .description("Entity Description")
                .durationMinutes(90)
                .price(BigDecimal.valueOf(200.0))
                .provider(provider)
                .build();

        OfferingResponseDTO dto = offeringMapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("Entity Offering");
        assertThat(dto.getDescription()).isEqualTo("Entity Description");
        assertThat(dto.getDurationMinutes()).isEqualTo(90);
        assertThat(dto.getPrice()).isEqualTo(BigDecimal.valueOf(200.0));
        assertThat(dto.getProviderId()).isEqualTo(2L);
    }

    @Test
    void testToDTO_shouldHandleNullProvider() {
        Offering entity = Offering.builder()
                .id(20L)
                .name("Entity Without Provider")
                .description("No provider description")
                .durationMinutes(30)
                .price(BigDecimal.valueOf(50.0))
                .provider(null)
                .build();

        OfferingResponseDTO dto = offeringMapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getProviderId()).isNull();
    }
}
