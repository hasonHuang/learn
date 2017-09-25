package com.hason.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.TreeSet;

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

    /**
     * 如果是自建服务器搭建Redis服务，一般是无需该配置即可运行；
     * 很多Redis云服务提供商考虑到安全因素，会禁用掉Redis的config命令;
     *
     * 解决方案：
     * 1. 配置Redis： redis-cli config set notify-keyspace-events Egx
     * 2. 配置Spring Session 不再执行config命令
     * 参考 http://docs.spring.io/spring-session/docs/current/reference/html5/#api-redisoperationssessionrepository-sessiondestroyedevent
     */
    @Bean
    public ConfigureRedisAction configureRedisAction() {
        // 令 Spring Session 不执行 config 命令
        return ConfigureRedisAction.NO_OP;
    }

    public static void main(String[] args) {
        TreeSet<Integer> set = new TreeSet<>();
        set.add(3);
        set.add(2);
        set.add(1);
        set.forEach(System.out::println);
    }
}
