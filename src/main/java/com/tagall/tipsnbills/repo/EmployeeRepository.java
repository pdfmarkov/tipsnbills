package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findById(Long id);

    List<Employee> findBySubsidiaryNameAndSubsidiaryOrganizationUsername(String subsidiaryName, String organizationName);

}
