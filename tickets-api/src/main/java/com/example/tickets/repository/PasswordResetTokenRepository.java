package com.example.tickets.repository;

import com.example.tickets.entity.PasswordResetToken;
import com.example.tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByCode(String code);
    void deleteByUser(User user);
}
