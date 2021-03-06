package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动本程序
 */
@SpringBootApplication
@EnableDiscoveryClient
// 使用 @EnableCircuitBreaker 或者 @EnableHystrix 启用断路器
@EnableCircuitBreaker
public class ConsumerMovieHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMovieHystrixApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
