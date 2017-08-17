package com.hason.springboot.config;

import com.hason.springboot.config.property.HttpEncodingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by Hason on 2017/8/17.
 */
@Configuration
// 开启属性注入
@EnableConfigurationProperties(HttpEncodingProperties.class)
// 当 CharacterEncodingFilter 在类路径的条件下
@ConditionalOnClass(CharacterEncodingFilter.class)
//@ConditionalOnClass(JedisConnection.class)
// 当设置 spring.http.encoding=enabled 的情况下，如果没有设置则默认为 true，即条件符合
@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)
public class HttpEncodingAutoConfig {

    @Autowired
    private HttpEncodingProperties httpEncodingProperties;

    // 配置 CharacterEncodingFilter
    @Bean
    // 当容器中没有这个Bean时新建一个
    @ConditionalOnMissingBean(CharacterEncodingFilter.class)
    public CharacterEncodingFilter characterEncodingFilter() {
        OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
        filter.setEncoding(httpEncodingProperties.getCharset().name());
        filter.setForceEncoding(httpEncodingProperties.isForce());
        return filter;
    }
}
