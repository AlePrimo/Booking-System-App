package com.aleprimo.Booking_System_App.entity;

import com.aleprimo.Booking_System_App.entity.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean sent = false;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User recipient;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }





}
