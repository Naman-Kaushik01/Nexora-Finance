package com.Nexora.NexoraFinance.auth_users.repo;

import com.Nexora.NexoraFinance.auth_users.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetCodeRepo extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByCode(String code);
    void deleteByUser(Long userId);
}
