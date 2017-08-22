package com.hason.controller;

import com.hason.repository.UserRepository;
import com.hason.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
