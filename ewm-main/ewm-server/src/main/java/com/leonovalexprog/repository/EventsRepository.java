package com.leonovalexprog.repository;

import com.leonovalexprog.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {
    @Query("SELECT CASE WHEN (count(*) > 0) THEN true ELSE false END " +
            "FROM Event AS e " +
            "WHERE e.category.id = ?1")
    boolean existsByCategory(long categoryId);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id = ?1")
    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);
}
