package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.Characteristic;
import com.tagall.tipsnbills.repo.CharacteristicRepository;
import com.tagall.tipsnbills.services.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CharacteristicServiceImpl implements CharacteristicService {

    @Autowired
    CharacteristicRepository characteristicRepository;

    @Override
    public List<Characteristic> findCharacteristicsWithSubsidiaryNameAndTime(String subsidiaryName, String organizationName, LocalDateTime time) {
        return characteristicRepository.findByEmployeeSubsidiaryNameAndEmployeeSubsidiaryOrganizationUsernameAndTimeIsGreaterThan(subsidiaryName, organizationName, time);
    }

    @Override
    public Long countCharacteristicsWithSubsidiaryAndNameAndTime(String subsidiaryName, String organizationName, LocalDateTime time) {
        return characteristicRepository.countByEmployeeSubsidiaryNameAndEmployeeSubsidiaryOrganizationUsernameAndTimeIsGreaterThan(subsidiaryName, organizationName, time);
    }

    @Override
    public Double avgRatingByEmployeeId(Long id) {
        return characteristicRepository.avgRatingByEmployeeId(id);
    }

    @Override
    public Characteristic saveCharacteristic(Characteristic characteristic) {
        return characteristicRepository.save(characteristic);
    }
}
