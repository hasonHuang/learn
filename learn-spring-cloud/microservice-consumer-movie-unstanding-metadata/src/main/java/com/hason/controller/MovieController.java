package com.hason.controller;

import com.hason.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Hason on 2017/8/22.
 */
@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private org.springframework.cloud.client.discovery.DiscoveryClient discoveryClient;

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable Long id) {
        return restTemplate.getForObject("http://localhost:8000/{id}", User.class, id);
    }

    @GetMapping("/user-instance")
    public List<ServiceInstance> showInfo() {
        return discoveryClient.getInstances("microservice-provider-user");
    }

}
