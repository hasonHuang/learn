package com.hason.feign;

import com.hason.entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Hason on 2017/8/23.
 */
// name 表示客户端的名称
@FeignClient(name = "microservice-provider-user", fallbackFactory = FeignClientFallbackFactory.class)
public interface UserFeignClient {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    User findById(@PathVariable("id") Long id);

}


/**
 * UserFeignClient 的 FallbackFacotry 类，该类需要实现 FallbackFactory 接口，并
 * 覆写 create 方法
 */
@Component
class FeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

    @Override
    public UserFeignClient create(Throwable throwable) {
        return new UserFeignClient() {
            @Override
            public User findById(@PathVariable("id") Long id) {
                // 日志最好放在各个 fallback 方法中，而不要直接放在create方法中
                // 否则在引用启动时，就会打印该日志
                // 详见 https://github.com/spring-cloud/spring-cloud-netflix/issues/1471
                logger.info("fallback; reason was:" + throwable);
                User user = new User();
                user.setId(-1L);
                user.setUsername("默认用户");
                return user;
            }
        };
    }

}