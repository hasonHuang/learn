package com.hason.dtp.message.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 消息队列的属性配置
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/11/7
 */
@Configuration
@ConfigurationProperties(prefix = "queue.message")
@PropertySource(value = "classpath:application.yml")
public class QueueMessageProperties {

    private String exampleQueue;
    private Integer maxRetry;
    private Integer expire;

    public String getExampleQueue() {
        return exampleQueue;
    }

    public void setExampleQueue(String exampleQueue) {
        this.exampleQueue = exampleQueue;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
