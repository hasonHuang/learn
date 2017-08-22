package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 1. 启动 microservice-simple-provider-user
 * 2. 启动本程序
 */
@SpringBootApplication
public class ConsumerMovieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMovieApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
