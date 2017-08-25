package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动 microservice-consumer-movie-feign-hystrix-fallback
 * 4. 启动本程序
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class, args);
    }

}
