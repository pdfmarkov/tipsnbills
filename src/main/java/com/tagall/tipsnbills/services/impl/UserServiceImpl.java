package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.User;
import com.tagall.tipsnbills.repo.UserRepository;
import com.tagall.tipsnbills.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
