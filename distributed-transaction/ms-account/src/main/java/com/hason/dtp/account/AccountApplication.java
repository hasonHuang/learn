package com.hason.dtp.account;

import com.hason.dtp.account.entity.User;
import com.hason.dtp.core.utils.converter.BooleanAttributeConverter;
import com.hason.dtp.message.entity.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 账户微服务
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/17
 */
@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackageClasses = { Message.class, User.class, BooleanAttributeConverter.class})
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

}
