package com.hason.springcache.controller;

import com.hason.springcache.entity.UserInfo;
import com.hason.springcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示缓存的API
 *
 * @author Hason
 */
@RestController
@RequestMapping(value = "/cache")
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user")
    public UserInfo get(Long userId) {
        return userService.get(userId);
    }

}
