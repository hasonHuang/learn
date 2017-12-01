package com.hason.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SSE 应用程序
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/12/1
 */
@EnableScheduling
@SpringBootApplication
public class SseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseApplication.class, args);
    }

}
