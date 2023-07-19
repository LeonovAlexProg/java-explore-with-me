package com.leonovalexprog.repository;

import com.leonovalexprog.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIdAndStatus(long eventId, ParticipationRequest.Status status);
}
