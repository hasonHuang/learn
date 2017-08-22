package com.hason;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by Hason on 2017/8/22.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaAuthenticatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaAuthenticatingApplication.class, args);
    }

}
