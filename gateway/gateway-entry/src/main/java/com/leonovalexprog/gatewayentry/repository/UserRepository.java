package com.leonovalexprog.gatewayentry.repository;

import com.leonovalexprog.gatewayentry.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}