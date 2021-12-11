package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {



}
