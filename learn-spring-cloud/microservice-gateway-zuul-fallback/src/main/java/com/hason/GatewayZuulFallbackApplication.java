package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 1. 启动 microservice-discovery-eureka
 * 2. 启动 microservice-provider-user
 * 3. 启动 microservice-consumer-movie-ribbon
 * 4. 启动 本程序
 *
 * 默认代理所有注册到 Eureka Server 的微服务，Zuul 路由规则：
 * http://ZUUL_HOST:ZUUL_PORT/微服务在EUREKA上的serviceId/**
 */
@SpringBootApplication
// 声明一个 Zuul 代理，该代理使用 Ribbon 来定位注册在 Eureka Server 中的微服务
// 同时，该代理还整合了 Hystrix 从而实现了容错，所有经过Zuul的请求都会在 Hystrix 命令中执行
@EnableZuulProxy
public class GatewayZuulFallbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayZuulFallbackApplication.class, args);
    }

}
