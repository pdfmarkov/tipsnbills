package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.Organization;
import com.tagall.tipsnbills.repo.OrganizationRepository;
import com.tagall.tipsnbills.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public Optional<Organization> findOrganizationByUsername(String username) {
        return organizationRepository.findByUsername(username);
    }

    @Override
    public Organization saveUser(Organization organization) {
        return organizationRepository.save(organization);
    }

}
