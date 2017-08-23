package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动多个 microservice-provider-user
 * 3. 启动本程序
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerMovieFeignLoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMovieFeignLoggingApplication.class, args);
    }

}
