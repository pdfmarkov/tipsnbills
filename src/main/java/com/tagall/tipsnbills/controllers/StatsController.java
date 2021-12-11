package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Characteristic;
import com.tagall.tipsnbills.module.responses.MessageResponseDto;
import com.tagall.tipsnbills.services.CharacteristicService;
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
import java.util.List;

@Controller
@CrossOrigin("*") // TODO : DELETE
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    CharacteristicService characteristicService;

    @Autowired
    OrganizationService organizationService;

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


    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
