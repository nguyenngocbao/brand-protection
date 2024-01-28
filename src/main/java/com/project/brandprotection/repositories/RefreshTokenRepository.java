package com.project.brandprotection.repositories;

import com.project.brandprotection.models.RefreshToken;
import com.project.brandprotection.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken findTokenByUser(User user);
}
