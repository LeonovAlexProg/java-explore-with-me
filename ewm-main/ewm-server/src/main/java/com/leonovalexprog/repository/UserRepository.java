package com.leonovalexprog.repository;

import com.leonovalexprog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
