package com.leonovalexprog.repository;

import com.leonovalexprog.model.Category;
import com.leonovalexprog.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {
    @Query("SELECT CASE WHEN (count(*) > 0) THEN true ELSE false END " +
            "FROM Event AS e " +
            "WHERE e.category.id = ?1")
    boolean existsByCategory(long categoryId);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id = ?1")
    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.category.id IN ?2 " +
            "AND e.state IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> findByUsersAndCategoriesWithTimestamp(List<Long> users, List<Long> categories, List<Event.State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.category.id IN ?2 " +
            "AND e.state IN ?3")
    List<Event> findByUsersAndCategories(List<Long> users, List<Long> categories, List<Event.State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4")
    List<Event> findByUsersWithTimestamp(List<Long> users, List<Event.State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2")
    List<Event> findByUsers(List<Long> users, List<Event.State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.category.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.eventDate BETWEEN ?3 AND ?4")
    List<Event> findByCategoriesWithTimestamp(List<Long> categories, List<Event.State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.category.id IN ?1 " +
            "AND e.state IN ?2")
    List<Event> findByCategories(List<Long> categories, List<Event.State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.state IN ?1 " +
            "AND e.eventDate BETWEEN ?2 AND ?3")
    List<Event> findWithTimestamp(List<Event.State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.state IN ?1")
    List<Event> findByStates(List<Event.State> states, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND e.participantLimit > (SELECT COUNT(r) FROM e.requests AS r WHERE r.status = 'CONFIRMED') " +
            "AND (lower(e.annotation) like concat('%', ?1, '%') OR lower(e.description) like concat('%', ?1, '%')) " +
            "AND e.category IN ?2 " +
            "AND e.paid IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> findPublicAvailable(String text, List<Category> categories, List<Boolean> paid, LocalDateTime rangeStartFilter, LocalDateTime rangeEndFilter, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (lower(e.annotation) like concat('%', ?1, '%') OR lower(e.description) like concat('%', ?1, '%')) " +
            "AND e.category IN ?2 " +
            "AND e.paid IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> findPublic(String text, List<Category> categories, List<Boolean> paid, LocalDateTime rangeStartFilter, LocalDateTime rangeEndFilter, Pageable pageable);
}
