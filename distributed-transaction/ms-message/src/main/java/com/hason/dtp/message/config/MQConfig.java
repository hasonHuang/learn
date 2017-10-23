package com.hason.dtp.message.config;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 消息队列配置
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/19
 */
//@Configuration
//@ConfigurationProperties(prefix = "queue.message")
//@PropertySource(value = "classpath:application.yml")
public class MQConfig {

    // 无需使用@Value
//    @Value("${userPoint}")
    private String userPoint;

    @Bean
    public Queue aBoolean() {
        // 创建一个持久化的队列
        return new Queue(userPoint, true);
    }

    public String getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(String userPoint) {
        this.userPoint = userPoint;
    }
}
