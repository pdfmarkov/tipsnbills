package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.Subsidiary;
import com.tagall.tipsnbills.repo.SubsidiaryRepository;
import com.tagall.tipsnbills.services.SubsidiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsidiaryServiceImpl implements SubsidiaryService {

    @Autowired
    SubsidiaryRepository subsidiaryRepository;

    @Override
    public Subsidiary saveSubsidiary(Subsidiary subsidiary) {
        return subsidiaryRepository.save(subsidiary);
    }
}
