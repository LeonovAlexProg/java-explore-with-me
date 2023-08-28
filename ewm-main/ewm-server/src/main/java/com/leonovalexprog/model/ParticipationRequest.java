package com.leonovalexprog.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Participation_Requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        REJECTED,
        CANCELED
    }
}
