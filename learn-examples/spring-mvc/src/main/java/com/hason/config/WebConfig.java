package com.hason.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 取代 web.xml
 *
 * WebApplicationInitializer 是 Spring 提供用来配置 Servlet3.0+ 配置的接口，从而实现替代 web.xml 的位置
 * 实现该接口会自动被 SpringServletContainerInitializer(用来启动 Servlet3.0 容器) 获取到
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/16
 */
public class WebConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 新建 WebApplicationContext，注册配置类，并和当前的 servletContext 关联
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MvcConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
        servlet.setAsyncSupported(true);    // 开启异步方法支持
    }

}
