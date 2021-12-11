package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByUsername(String username);

    Optional<Organization> findById(Long id);

    Boolean existsByUsername(String username);

}
