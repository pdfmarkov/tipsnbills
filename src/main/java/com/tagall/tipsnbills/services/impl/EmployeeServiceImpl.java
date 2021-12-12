package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.Employee;
import com.tagall.tipsnbills.repo.EmployeeRepository;
import com.tagall.tipsnbills.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getEmployeesForStats(String subsidiaryName, String organizationName) {
        return employeeRepository.findBySubsidiaryNameAndSubsidiaryOrganizationUsername(subsidiaryName, organizationName);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
