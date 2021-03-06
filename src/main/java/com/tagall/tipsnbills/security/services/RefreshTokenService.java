package com.tagall.tipsnbills.security.services;

import com.tagall.tipsnbills.exceptions.TokenRefreshException;
import com.tagall.tipsnbills.module.RefreshToken;
import com.tagall.tipsnbills.repo.RefreshTokenRepository;
import com.tagall.tipsnbills.repo.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${token.refresh_time}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long organizationId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setOrganization(organizationRepository.findById(organizationId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByOrganizationId(Long userId) {
        return refreshTokenRepository.deleteByOrganization(organizationRepository.findById(userId).get());
    }
}
