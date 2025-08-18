package com.aleprimo.Booking_System_App.service;


import com.aleprimo.Booking_System_App.entity.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OfferingService {
    Offering createOffering(Offering offering);
    Offering updateOffering(Long id, Offering offering);
    void deleteOffering(Long id);
    Optional<Offering> getOfferingById(Long id);
    Page<Offering> getAllOfferings(Pageable pageable);
    Page<Offering> getOfferingsByProviderId(Long providerId, Pageable pageable);
}
