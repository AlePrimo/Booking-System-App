package com.aleprimo.Booking_System_App.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "offerings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer durationMinutes;

    @NotNull
    @Column(nullable = false)
    private Double price;


    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;
}
