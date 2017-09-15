package com.hason.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
}
