package com.leonovalexprog.repository;

import com.leonovalexprog.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventsRepository extends JpaRepository<Event, Long> {
    @Query("SELECT CASE WHEN (count(*) > 0) THEN true ELSE false END " +
            "FROM Event AS e " +
            "WHERE e.category.id = ?1")
    boolean existsByCategory(long categoryId);
}
