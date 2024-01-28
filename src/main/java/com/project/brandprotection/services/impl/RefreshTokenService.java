package com.project.brandprotection.services.impl;

import com.project.brandprotection.exceptions.TokenRefreshException;
import com.project.brandprotection.models.RefreshToken;
import com.project.brandprotection.models.User;
import com.project.brandprotection.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.expiration-refresh-token-milliseconds}")
    private long refreshTokenExpirationDate;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isTokenExpired()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken existingToken = refreshTokenRepository.findTokenByUser(user);
        if (existingToken != null) {
            refreshTokenRepository.delete(existingToken);
        }
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshTokenExpirationDate);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(expireDate)
                .token(UUID.randomUUID().toString())
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

}
