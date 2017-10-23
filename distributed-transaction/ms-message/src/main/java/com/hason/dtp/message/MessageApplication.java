package com.hason.dtp.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 可靠消息服务程序入口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/23
 */
@SpringBootApplication
@EnableEurekaClient
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

}
