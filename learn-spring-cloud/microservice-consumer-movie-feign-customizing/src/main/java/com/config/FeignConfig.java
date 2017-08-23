package com.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign的配置类
 * 注意：不应该出现在主应用程序上下文的 @ComponentScan 中
 */
@Configuration
public class FeignConfig {

    /**
     * 将契约改为feign原生的默认契约。这样就可以使用feign自带的注解了。
     * @return 默认的feign契约
     */
    @Bean
    public Contract feignContract() {
        return new Contract.Default();
    }
}
