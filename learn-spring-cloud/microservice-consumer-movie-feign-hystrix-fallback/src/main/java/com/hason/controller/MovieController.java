package com.hason.controller;

import com.hason.entity.User;
import com.hason.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hason on 2017/8/22.
 */
@RestController
public class MovieController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

}
