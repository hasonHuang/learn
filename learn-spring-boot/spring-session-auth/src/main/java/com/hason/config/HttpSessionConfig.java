package com.hason.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 开启 Redis Http Session
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/14
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

    // 将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID
    // CookiePath设置为根路径且配置了相关的正则表达式，可以达到同父域下的单点登录
    // 的效果，在未涉及跨域的单点登录系统中，这是一个非常优雅的解决方案
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }

}
