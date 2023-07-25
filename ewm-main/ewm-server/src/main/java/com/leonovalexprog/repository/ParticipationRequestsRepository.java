package com.leonovalexprog.repository;

import com.leonovalexprog.model.ParticipationRequest;
import com.leonovalexprog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIdAndStatus(long eventId, ParticipationRequest.Status status);

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "WHERE r.requester = ?1")
    List<ParticipationRequest> findAllByUser(User user);
}
