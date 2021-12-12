package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    Optional<Subsidiary> findById(Long id);

}
