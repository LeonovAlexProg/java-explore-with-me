package com.leonovalexprog.repository;

import com.leonovalexprog.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location AS l " +
            "WHERE l.lat = ?1 " +
            "AND l.lon = ?2")
    List<Location> findByExactCoordinates(Float lat, Float lon);

    @Query("SELECT l FROM Location AS l " +
            "WHERE (l.lat * l.lat) + (l.lon * l.lon) <= (l.rad * l.rad)")
    List<Location> findByCoordinates(Float lat, Float lon, Float rad);

    @Query("SELECT l FROM Location AS l " +
            "WHERE (l.lat - ?1) * (l.lat - ?1) + (l.lon - ?2) * (l.lon - ?2) <= l.rad * l.rad")
    List<Location> findByEventCoordinates(Float lat, Float lon);
}
