package com.leonovalexprog.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        REJECTED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationRequest that = (ParticipationRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(created, that.created) && Objects.equals(event, that.event) && Objects.equals(requester, that.requester) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, event, requester, status);
    }
}
