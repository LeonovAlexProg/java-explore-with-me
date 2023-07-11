package com.leonovalexprog.repository;

import com.leonovalexprog.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request AS r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2")
    List<Request> findByTimestamp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT r FROM Request AS r " +
            "WHERE r.timestamp BETWEEN ?1 AND ?2 " +
            "AND r.uri IN ?3")
    List<Request> findByTimestampAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
