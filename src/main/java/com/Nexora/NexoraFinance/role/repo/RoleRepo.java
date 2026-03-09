package com.Nexora.NexoraFinance.role.repo;

import com.Nexora.NexoraFinance.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
