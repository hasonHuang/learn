package com.hason.config;

import com.config.RibbonConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * 使用 RibbonClient, 为特定 name 的 Ribbon Client 自定义配置
 * 使用 @RibbonClient 的 configuration 属性，指定 Ribbon 的配置类
 * 可参考的示例：
 * http://spring.io/guides/gs/client-side-load-balancing/
 */
@Configuration
@RibbonClient(name = "microservice-provider-user", configuration = RibbonConfig.class)
public class TestConfig {
}
