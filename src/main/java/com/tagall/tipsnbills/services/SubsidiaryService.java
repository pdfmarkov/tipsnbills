package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Subsidiary;

import java.util.Optional;

public interface SubsidiaryService {

    Optional<Subsidiary> findSubsidiaryById(Long id);

    Subsidiary saveSubsidiary(Subsidiary subsidiary);
}
