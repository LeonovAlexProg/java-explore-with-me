package com.leonovalexprog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.leonovalexprog.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
