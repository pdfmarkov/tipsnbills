package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);

    User saveUser(User user);

}
