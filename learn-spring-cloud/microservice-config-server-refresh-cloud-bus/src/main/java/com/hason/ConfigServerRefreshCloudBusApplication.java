package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 声明这是一个配置服务器
 *
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerRefreshCloudBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerRefreshCloudBusApplication.class, args);
    }

}
