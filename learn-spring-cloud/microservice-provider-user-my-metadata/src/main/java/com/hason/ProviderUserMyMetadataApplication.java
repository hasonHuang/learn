package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动
 */
@SpringBootApplication
// @EnableDiscoveryClient 更通用、抽象
//@EnableEurekaClient
@EnableDiscoveryClient
public class ProviderUserMyMetadataApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderUserMyMetadataApplication.class, args);
    }
}
