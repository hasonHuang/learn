package com.hason.controller;

import com.hason.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Hason on 2017/8/22.
 */
@RestController
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable Long id) {
        // 请求地址改成了http://microservice-provider-user
        return restTemplate.getForObject("http://microservice-provider-user/{id}", User.class, id);
    }

    @GetMapping("/log-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-provider-user");
        // 打印当前选择的是哪个节点
        logger.info("{}:{}:{}",
                serviceInstance.getServiceId(),
                serviceInstance.getHost(),
                serviceInstance.getPort());
    }

}
