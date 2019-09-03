package com.hason.springcache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 开启 Spring Cache，{@link org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration} 会自动创建一个 RedisCacheConfiguration
 *
 * @author Hason
 */
@EnableCaching
@Configuration
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager() {
//        // configure and return an implementation of Spring's CacheManager SPI
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default")));
//        return cacheManager;
//    }

}
