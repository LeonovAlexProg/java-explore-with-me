package com.leonovalexprog.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    private String annotation;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @OneToMany
    @JoinColumn(name = "requests_id")
    private List<ParticipationRequest> requests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @OneToOne
    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Long views;

    public enum State {
        PENDING,
        PUBLISHED,
        CANCELED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(category, event.category) && Objects.equals(requests, event.requests) && Objects.equals(createdOn, event.createdOn) && Objects.equals(description, event.description) && Objects.equals(eventDate, event.eventDate) && Objects.equals(initiator, event.initiator) && Objects.equals(location, event.location) && Objects.equals(paid, event.paid) && Objects.equals(participantLimit, event.participantLimit) && Objects.equals(publishedOn, event.publishedOn) && Objects.equals(requestModeration, event.requestModeration) && Objects.equals(state, event.state) && Objects.equals(title, event.title) && Objects.equals(views, event.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, requests, createdOn, description, eventDate, initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }
}
