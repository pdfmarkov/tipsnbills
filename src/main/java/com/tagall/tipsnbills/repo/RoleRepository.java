package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.ERole;
import com.tagall.tipsnbills.module.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
