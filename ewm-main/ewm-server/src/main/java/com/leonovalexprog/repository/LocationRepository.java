package com.leonovalexprog.repository;

import com.leonovalexprog.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}