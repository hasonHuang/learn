package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 从配置服务器中读取配置的客户端
 */
@SpringBootApplication
public class ConfigClientRefreshCloudBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientRefreshCloudBusApplication.class, args);
    }

}
