package com.hason.controller;

import com.hason.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    // 指定一个回退方法处理回退的逻辑。回退方法的参数应该与被代理的方法(findById)参数一致
    // Hystrix 配置属性可详见 Wiki: https://github.com/Netflix/Hystrix/wiki/Configuration
    @HystrixCommand(fallbackMethod = "findByIdFallback"
//    , commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
//            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
//    }, threadPoolProperties = {
//            @HystrixProperty(name = "coreSize", value = "1"),
//            @HystrixProperty(name = "maxQueueSize", value = "10")}
    )
    @GetMapping(value = "/users/{id}")
    public User findById(@PathVariable Long id) {
        // 请求地址改成了http://microservice-provider-user
        return restTemplate.getForObject("http://microservice-provider-user/{id}", User.class, id);
    }

    // 回退方法
    private User findByIdFallback(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("默认用户");
        return user;
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
