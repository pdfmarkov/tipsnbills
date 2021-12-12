package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Characteristic;
import com.tagall.tipsnbills.module.Employee;
import com.tagall.tipsnbills.module.responses.EmployeeForTableResponseDto;
import com.tagall.tipsnbills.services.CharacteristicService;
import com.tagall.tipsnbills.services.EmployeeService;
import com.tagall.tipsnbills.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@CrossOrigin("*") // TODO : DELETE
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    CharacteristicService characteristicService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/allsum")
    public ResponseEntity<?> getSumMoneyFromPreviousMonth(@RequestParam String subsidiaryName) {
        List<Characteristic> characteristics = characteristicService.findCharacteristicsWithSubsidiaryNameAndTime(
                subsidiaryName, getCurrentUsername(), LocalDateTime.now().minusMonths(1));
        if (characteristics.isEmpty()) return ResponseEntity.ok(0);
        Long money = 0L;
        for (Characteristic characteristic : characteristics)
            money += characteristic.getMoney();
        return ResponseEntity.ok(money);
    }

    @GetMapping("/countfeedback")
    public ResponseEntity<?> getCountFeedbackFromPreviousMonth(@RequestParam String subsidiaryName) {
        return ResponseEntity.ok(characteristicService.countCharacteristicsWithSubsidiaryAndNameAndTime(
                subsidiaryName, getCurrentUsername(), LocalDateTime.now().minusMonths(1)));
    }

    @GetMapping("/avgrating")
    public ResponseEntity<?> getAvgRatingFromPreviousTwoWeeks(@RequestParam String subsidiaryName) {
        List<Characteristic> characteristics = characteristicService.findCharacteristicsWithSubsidiaryNameAndTime(
                subsidiaryName, getCurrentUsername(), LocalDateTime.now().minusWeeks(2));
        if (characteristics.isEmpty()) throw new ResourceNotFoundException("Error: Characteristics Not Found");
        List<Double> avgRatingList = new ArrayList<>();
        LocalDateTime endDate, startDate;
        Double temp;
        int size;


        for (int i = 13; i >= 0; i-- ) {
            endDate = LocalDateTime.now().minusDays(i);
            startDate = LocalDateTime.now().minusDays(i+1);
            temp = 0.0; size = 0;
            for (Characteristic characteristic : characteristics)
                if (characteristic.getTime().compareTo(startDate) > 0 && characteristic.getTime().compareTo(endDate) < 0) {
                    size++;
                    temp += characteristic.getRating();
                }
            avgRatingList.add(temp/size);
        }

        return ResponseEntity.ok(avgRatingList);
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees(@RequestParam String subsidiaryName) {
        List<Employee> employees = employeeService.getEmployeesForStats(subsidiaryName, getCurrentUsername());
        if (employees.isEmpty()) throw new ResourceNotFoundException("Error: Employees Not Found");
        List<EmployeeForTableResponseDto> employeeForTableResponseDtoList = new ArrayList<>();
        for (Employee employee : employees)
            employeeForTableResponseDtoList.add(new EmployeeForTableResponseDto(
                    employee.getName(),
                    employee.getSurname(),
                    characteristicService.avgRatingByEmployeeId(employee.getId()),
                    employee.getEmail(),
                    employee.getCardNumber()
            ));
        return ResponseEntity.ok(employeeForTableResponseDtoList);
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

}
