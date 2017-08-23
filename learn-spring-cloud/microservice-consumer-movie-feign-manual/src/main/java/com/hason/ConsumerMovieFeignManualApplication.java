package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动多个 microservice-provider-user-with-auth
 * 3. 启动本程序
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerMovieFeignManualApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMovieFeignManualApplication.class, args);
    }

}
