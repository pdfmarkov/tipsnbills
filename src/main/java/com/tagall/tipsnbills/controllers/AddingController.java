package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.services.CharacteristicService;
import com.tagall.tipsnbills.services.EmployeeService;
import com.tagall.tipsnbills.services.OrganizationService;
import com.tagall.tipsnbills.services.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
