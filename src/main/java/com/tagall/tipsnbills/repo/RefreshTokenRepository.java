package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.RefreshToken;
import com.tagall.tipsnbills.module.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByOrganization(Organization organization);
}