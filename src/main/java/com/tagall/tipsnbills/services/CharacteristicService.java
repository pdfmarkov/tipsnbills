package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Characteristic;

import java.time.LocalDateTime;
import java.util.List;


public interface CharacteristicService {

    List<Characteristic> findCharacteristicsWithSubsidiaryNameAndTime(String subsidiaryName, LocalDateTime time);
}
