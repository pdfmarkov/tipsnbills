package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Organization;

import java.util.Optional;

public interface UserService {

    Optional<Organization> findOrganizationByUsername(String username);

    Organization saveUser(Organization organization);

}
