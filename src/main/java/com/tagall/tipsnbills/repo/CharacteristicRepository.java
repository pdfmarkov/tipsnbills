package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {

    List<Characteristic> findByEmployeeSubsidiaryNameAndEmployeeSubsidiaryOrganizationUsernameAndTimeIsGreaterThan(String subsidiaryName, String organizationName, LocalDateTime time);

    List<Characteristic> findByEmployeeId(Long id);

    List<Characteristic> findByEmployeeSubsidiaryNameAndEmployeeSubsidiaryOrganizationUsername(String subsidiaryName, String organizationName);

    @Query(value = "select avg(c.rating) from Characteristic c " +
            "join Employee e on c.employee.id = e.id " +
            "where c.employee.id = ?1")
    Double avgRatingByEmployeeId(Long id);

    Long countByEmployeeSubsidiaryNameAndEmployeeSubsidiaryOrganizationUsernameAndTimeIsGreaterThan(String subsidiaryName, String organizationName, LocalDateTime time);

}
