package com.leonovalexprog.repository;

import com.leonovalexprog.model.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<EventLocation, Long> {
    Boolean existsByLatAndLon(float lat, float lon);

    EventLocation findByLatAndLon(Float lat, Float lon);
}
