package com.aleprimo.Booking_System_App.repository;


import com.aleprimo.Booking_System_App.entity.Offering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {
    List<Offering> findByProviderId(Long providerId);
}
