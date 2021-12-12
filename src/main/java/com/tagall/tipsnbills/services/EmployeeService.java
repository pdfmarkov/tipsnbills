package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> findEmployeeById(Long id);

    List<Employee> getEmployeesForStats(String subsidiaryName, String organizationName);

    Employee saveEmployee(Employee employee);

}
