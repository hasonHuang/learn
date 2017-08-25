package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动 microservice-consumer-movie-ribbon-hystrix-turbine-mq
 * 5. 启动 (本程序)
 * 7. 访问 http://localhost:8010/users/1
 * 8. 打开 http://localhost:8031
 */
@SpringBootApplication
@EnableTurbineStream
public class HystrixTurbineMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixTurbineMqApplication.class, args);
    }

}
