package com.hason.autoconfig.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 属性配置
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/19
 */
@Component
@ConfigurationProperties(prefix = "hason.hello")
@PropertySource(value = {"classpath:hason.yml", "classpath:hason.properties"},
        ignoreResourceNotFound = true, factory = YamlPropertySourceFactory.class)
public class HelloProperties {

    /** 默认消息 */
    private static final String MSG = "hello";

    /** 消息 */
    private String msg = MSG;

    public static String getMSG() {
        return MSG;
    }

    public String getMsg() {
        return msg;
    }

    public HelloProperties setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}

/**
 * 自定义的YAML配置资源策略，兼容 .properties
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/19
 */
class YamlPropertySourceFactory implements PropertySourceFactory {

    private PropertySourceFactory PropertySourceFactory =
            new DefaultPropertySourceFactory();

    @Override
    public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        // 判断资源文件后缀，仅当.yml才使用 YAML loader
        if (resource.getResource().getFilename().endsWith(".yml"))
            return yamlLoader(name, resource);
        else
            return PropertySourceFactory.createPropertySource(name, resource);
    }

    // 该方法可以忽略文件不存在
    private org.springframework.core.env.PropertySource<?> yamlLoader(String name, EncodedResource resource) throws IOException {
        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        if (name != null)
            return yamlLoader.load(name, resource.getResource(), null);
        else {
            // 捕获 IllegalStateException, 如果cause是文件不存在，则抛出 FileNotFoundException
            // 这样可以临时解决自定义配置资源策略导致 PropertySource.ignoreResourceNotFound 失效
            // 原因：ConfigurationClassParser 调用该接口方法 createPropertySource 时没有捕获 IllegalStateException
            try {
                return yamlLoader.load(getNameForResource(resource.getResource()), resource.getResource(), null);
            } catch (IllegalStateException e) {
                if (e.getCause() instanceof FileNotFoundException)
                    throw (FileNotFoundException) e.getCause();
                throw e;
            }
        }
    }

    // 该方法无法忽略文件不存在（PropertySource.ignoreResourceNotFound 失效）
    // 可以参考 yamlLoader 判断异常再抛出
    private org.springframework.core.env.PropertySource<?> sourceLoader(String name, EncodedResource resource) throws IOException {
        PropertySourcesLoader loader = new PropertySourcesLoader();
        return name != null ? loader.load(resource.getResource(), name, null) : loader.load(
                resource.getResource(), getNameForResource(resource.getResource()), null);
    }

    // 参考 DefaultPropertySourceFactory
    private static String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!org.springframework.util.StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }
}