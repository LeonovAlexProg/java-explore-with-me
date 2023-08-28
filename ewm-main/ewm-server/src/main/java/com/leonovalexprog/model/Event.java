package com.leonovalexprog.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "event")
    private List<ParticipationRequest> requests;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @OneToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    @Column(nullable = false)
    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(length = 120, nullable = false)
    private String title;

    public enum State {
        PENDING,
        PUBLISHED,
        CANCELED,
    }
}
