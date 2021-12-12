package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Characteristic;
import com.tagall.tipsnbills.module.Employee;
import com.tagall.tipsnbills.module.Subsidiary;
import com.tagall.tipsnbills.module.requested.CreateCharacteristicDto;
import com.tagall.tipsnbills.module.requested.CreateEmployeeDto;
import com.tagall.tipsnbills.module.requested.CreateSubsidiaryDto;
import com.tagall.tipsnbills.module.responses.EmployeeResponseDto;
import com.tagall.tipsnbills.module.responses.SubsidiaryResponseDto;
import com.tagall.tipsnbills.services.CharacteristicService;
import com.tagall.tipsnbills.services.EmployeeService;
import com.tagall.tipsnbills.services.OrganizationService;
import com.tagall.tipsnbills.services.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@CrossOrigin("*") // TODO : DELETE
@RequestMapping("/api/add")
public class AddingController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    SubsidiaryService subsidiaryService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CharacteristicService characteristicService;

    @PostMapping("/subsidiary")
    public ResponseEntity<?> addSubsidiary(@Valid @RequestBody CreateSubsidiaryDto createSubsidiaryDto) {

        Subsidiary subsidiary = new Subsidiary();

        subsidiary.setName(createSubsidiaryDto.getSubsidiary());
        subsidiary.setOrganization(organizationService.findOrganizationByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Organization Not Found")));

        Subsidiary subsidiaryResponse = subsidiaryService.saveSubsidiary(subsidiary);

        return ResponseEntity.ok(new SubsidiaryResponseDto(subsidiaryResponse.getId(), subsidiaryResponse.getName()));

    }

    @PostMapping("/employee")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto) {

        Employee employee = new Employee();

        employee.setName(createEmployeeDto.getName());
        employee.setSurname(createEmployeeDto.getSurname());
        employee.setEmail(createEmployeeDto.getEmail());
        employee.setCardNumber(createEmployeeDto.getCardNumber());

        employee.setSubsidiary(subsidiaryService.findSubsidiaryById(createEmployeeDto.getSubsidiaryId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Subsidiary Not Found")));

        Employee employeeResponse = employeeService.saveEmployee(employee);

        return ResponseEntity.ok(new EmployeeResponseDto(
                employeeResponse.getId(),
                employeeResponse.getName(),
                employeeResponse.getSurname(),
                employeeResponse.getCardNumber(),
                employeeResponse.getEmail()
        ));

    }

    @PostMapping("/feedback")
    public ResponseEntity<?> addFeedback(@Valid @RequestBody CreateCharacteristicDto createCharacteristicDto) {

        Characteristic characteristic = new Characteristic();

        if (createCharacteristicDto.getReview() != null && !createCharacteristicDto.getReview().trim().equals(""))
            characteristic.setReview(createCharacteristicDto.getReview());
        if (createCharacteristicDto.getRating() != null && createCharacteristicDto.getRating() <= 5  && createCharacteristicDto.getRating() >= 1)
            characteristic.setRating(createCharacteristicDto.getRating());
        if (createCharacteristicDto.getMoney() != null && !createCharacteristicDto.getReview().trim().equals(""))
            characteristic.setMoney(createCharacteristicDto.getMoney());

        characteristic.setTime(LocalDateTime.now());

        characteristic.setEmployee(employeeService.findEmployeeById(createCharacteristicDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Employee Not Found")));

        characteristicService.saveCharacteristic(characteristic);

        return ResponseEntity.ok("Feedback successfully added!");

    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
