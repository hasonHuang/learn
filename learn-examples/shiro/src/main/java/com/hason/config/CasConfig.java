package com.hason.config;

import com.hason.realm.MyCasRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * 集成 CAS 配置
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/11
 */
@Configuration
public class CasConfig {

    private static final Logger logger = LoggerFactory.getLogger(CasConfig.class);

    // CasServerUrlPrefix
    public static final String casServerUrlPrefix = "https://localhost:8443/cas";
    // Cas登录页面地址
    public static final String casLoginUrl = casServerUrlPrefix + "/login";
    // Cas登出页面地址
    public static final String casLogoutUrl = casServerUrlPrefix + "/logout";
    // 当前工程对外提供的服务地址
    public static final String shiroServerUrlPrefix = "https://localhost:8443";
    // casFilter UrlPattern
    public static final String casFilterUrlPattern = "/loginSuccess";
    // 登录地址
    public static final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern;

//    @Bean
//    public EhCacheManager ehCacheManager() {
//        EhCacheManager em = new EhCacheManager();
//        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
//        return em;
//    }

    // 配置CAS Realm
    @Bean
    public CasRealm casRealm() {
        MyCasRealm realm = new MyCasRealm();
//        realm.setCacheManager(ehCacheManager());
        return realm;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 下面的名字就是 ShiroFilterFactoryBean 的名字
        filterRegistrationBean.setFilter(new DelegatingFilterProxy("shiroFilterFactoryBean"));
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    // 使用 CAS
    @Bean
    public SecurityManager securityManager(CasRealm casRealm, SessionManager sessionManager, RememberMeManager rememberMeManager) {
        // 必须使用 DefaultWebSecurityManager，否则启动时报错 SecurityManager 没有实现 WebSecurityManager 接口
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(casRealm);                            // 设置数据源
        securityManager.setSubjectFactory(new CasSubjectFactory());
        securityManager.setSessionManager(sessionManager);          // 设置会话管理器
        securityManager.setRememberMeManager(rememberMeManager);  // 设置RememberMe管理器

        SecurityUtils.setSecurityManager(securityManager);
        // <!-- 相当于调用 SecurityUtils.setSecurityManager(securityManager) -->
        // <bean class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
        // <property name="staticMethod"
        //         value="org.apache.shiro.SecurityUtils.setSecurityManager"/>
        // <property name="arguments" ref="securityManager"/>
        // </bean>

        return securityManager;
    }

    // CAS过滤器
    @Bean
    public CasFilter casFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
        casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面
        return casFilter;
    }

}
