package com.hason.controller;

import com.hason.entity.User;
import com.hason.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hason on 2017/8/22.
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/{id}")
    public User findById(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        return user;
    }

}
