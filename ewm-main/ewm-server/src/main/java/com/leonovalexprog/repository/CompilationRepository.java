package com.leonovalexprog.repository;

import com.leonovalexprog.model.Compilation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation as c " +
            "WHERE c.pinned IN ?1")
    List<Compilation> findAllWherePinned(List<Boolean> pinnedFilter, Pageable pageable);
}
