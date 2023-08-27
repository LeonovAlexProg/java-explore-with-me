package com.leonovalexprog.gatewayentry.repository;

import com.leonovalexprog.gatewayentry.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
