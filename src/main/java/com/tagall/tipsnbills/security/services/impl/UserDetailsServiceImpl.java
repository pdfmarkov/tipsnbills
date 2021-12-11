package com.tagall.tipsnbills.security.services.impl;

import com.tagall.tipsnbills.module.Organization;
import com.tagall.tipsnbills.repo.OrganizationRepository;
import com.tagall.tipsnbills.security.module.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Organization organization = organizationRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(organization);
    }
}
