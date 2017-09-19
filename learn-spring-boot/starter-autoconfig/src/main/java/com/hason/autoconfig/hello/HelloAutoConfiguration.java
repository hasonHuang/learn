package com.hason.autoconfig.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hason.service.HelloService;

/**
 * 自动配置类，满足条件时触发。
 * 在 spring.factories 文件注册 HelloAutoConfiguration
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/19
 */
@Configuration
//@EnableConfigurationProperties(HelloProperties.class)
// 仅当类路径中存在 HelloService 时才触发自动配置
@ConditionalOnClass(HelloService.class)
// 当设置 hason.hello=enabled 的情况（没有设置默认true）,配置前缀为 hason.hello 的配置时才触发
@ConditionalOnProperty(prefix = "hason.hello", value = "enabled", matchIfMissing = true)
public class HelloAutoConfiguration {

    @Autowired
    private HelloProperties helloProperties;

    // 仅当容器中不存在 HelloService 实例时，才执行该方法
    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService() {
        HelloService service = new HelloService();
        service.setMsg(helloProperties.getMsg());
        return service;
    }

}
