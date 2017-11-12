package com.hason.dtp.account.config;

import com.hason.dtp.account.config.properties.QueueMessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 消息队列配置
 *
 * 为了示范另一种方式的配置文件注入方式，这里不使用 {@link QueueMessageProperties}
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/19
 */
@Configuration
@ConfigurationProperties(prefix = "queue.message")
@PropertySource(value = "classpath:application.yml")
public class MQConfig {

    // 无需使用@Value
//    @Value("${userPoint}")
    private String userPointQueue;

    @Bean
    public Queue userPointQueue() {
        // 创建一个持久化的队列
        return new Queue(userPointQueue, true);
    }

    public String getUserPointQueue() {
        return userPointQueue;
    }

    public void setUserPointQueue(String userPointQueue) {
        this.userPointQueue = userPointQueue;
    }
}
