package com.hason.dtp.tcc.account.config;

import com.hason.dtp.core.exception.RestExceptionHandler;
import com.hason.dtp.core.support.FormModelMethodArgumentResolver;
import com.hason.dtp.core.support.HandlerMethodMultiArgumentResolver;
import feign.Feign;
import feign.codec.Encoder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * MVC 配置类
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/10/23
 */
@Configuration
@ComponentScan(basePackageClasses = RestExceptionHandler.class)
public class MvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new HandlerMethodMultiArgumentResolver());
        argumentResolvers.add(new FormModelMethodArgumentResolver());
    }

}
