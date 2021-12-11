package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Characteristic;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {

    List<Characteristic> findByEmployeeSubsidiaryNameAndTimeIsGreaterThan(String name, LocalDateTime time);

}
