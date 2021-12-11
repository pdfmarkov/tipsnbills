package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Organization;

import java.util.Optional;

public interface OrganizationService {

    Optional<Organization> findOrganizationByUsername(String username);

    Organization saveUser(Organization organization);

}
