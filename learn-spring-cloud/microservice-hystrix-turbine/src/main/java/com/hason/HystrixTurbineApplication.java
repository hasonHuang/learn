package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动 microservice-consumer-movie-ribbon-hystrix
 * 4. 启动 microservice-consumer-movie-feign-hystrix-fallback-stream
 * 5. 启动 (本程序) microservice-hystrix-turbine
 * 6. 启动 microservice-hystrix-dashboard
 * 7. 访问 http://localhost:8010/users/1，让microservice-consumer-movie-ribbon-hystrix产生监控数据
 * 8. 打开 http://localhost:8030/hystrix.stream, 填入 http://localhost:8031/turbine.stream
 */
@SpringBootApplication
@EnableTurbine
public class HystrixTurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixTurbineApplication.class, args);
    }

}
