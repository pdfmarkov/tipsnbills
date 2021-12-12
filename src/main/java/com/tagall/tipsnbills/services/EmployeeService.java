package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Employee;

import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> findEmployeeById(Long id);

    Employee saveEmployee(Employee employee);

}
