package com.hason.feign;

import com.config.FeignConfig;
import com.hason.entity.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 使用 configuration 属性，指定配置类
 */
// name 表示客户端的名称
@FeignClient(name = "microservice-provider-user", configuration = FeignConfig.class)
public interface UserFeignClient {

    /**
     * 使用 feign 自带的注解 @RequestLine
     *
     * @see https://github.com/OpenFeign/feign
     * @param id 用户id
     * @return 用户信息
     */
    @RequestLine("GET /{id}")
    User findById(@Param("id") Long id);

}
