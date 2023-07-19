package com.leonovalexprog.repository;

import com.leonovalexprog.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRequestsRepository extends JpaRepository<ParticipationRequest, Long> {
}
