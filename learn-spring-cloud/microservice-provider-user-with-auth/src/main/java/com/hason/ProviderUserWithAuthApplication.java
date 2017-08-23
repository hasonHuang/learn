package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 本程序
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProviderUserWithAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderUserWithAuthApplication.class, args);
    }
}
