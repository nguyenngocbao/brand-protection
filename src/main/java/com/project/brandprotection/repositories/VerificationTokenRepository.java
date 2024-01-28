package com.project.brandprotection.repositories;

import com.project.brandprotection.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT t from VerificationToken t JOIN FETCH t.user WHERE t.token = :token")
    VerificationToken findByToken(String token);
}