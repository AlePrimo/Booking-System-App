package com.aleprimo.Booking_System_App.entity;

import com.aleprimo.Booking_System_App.entity.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FutureOrPresent
    @NotNull
    @Column(nullable = false)
    private LocalDateTime bookingDateTime;
@NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;


    @ManyToOne
    @JoinColumn(name = "offering_id", nullable = false)
    private Offering offering;
}
