package com.hason.config;

import com.hason.realm.RetryLimitHashedCredentialsMatcher;
import com.hason.realm.UserRealm;
import com.hason.service.UserService;
import com.hason.util.PasswordHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置用户源
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/28
 */
@Configuration
public class ShiroConfig {

    // 配置 Shiro 的 Web 过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        //Shiro的核心安全接口,这个属性是必须的
        factoryBean.setSecurityManager(securityManager);
        // 要求登录时的链接(可根据项目的URL进行替换),非必须的属性,默认会自动寻找Web工程根目录下的"/login.jsp"页面
        factoryBean.setLoginUrl("/loginPage");
        // 登录成功后要跳转的连接,逻辑也可以自定义，例如返回上次请求的页面
        factoryBean.setSuccessUrl("/loginSuccess");
        // 用户访问未对其授权的资源时,所显示的连接
        factoryBean.setUnauthorizedUrl("/403");


        /*
         * 定义shiro过滤链  Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 配置静态资源过滤器，无需登录即可访问（匿名访问）
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "logout");

        // url=拦截器[参数]，拦截器[参数]
        filterChainDefinitionMap.put("/role", "authc,roles[admin]");
        filterChainDefinitionMap.put("/permission", "authc,perms[user:create]");
        filterChainDefinitionMap.put("/authcBasic", "authcBasic,roles[admin]");

        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边; 这是一个坑呢，一不小心代码就不好使了;
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");

        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }

    @Bean
    public SecurityManager securityManager(UserRealm realm) {
        // 必须使用 DefaultWebSecurityManager，否则启动时报错 SecurityManager 没有实现 WebSecurityManager 接口
//        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public UserRealm userRealm(UserService userService, PasswordHelper passwordHelper) {

        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        // 此处要与加密时的次数一致，否者一直无法通过验证
        matcher.setHashIterations(passwordHelper.getHashIterations());
        matcher.setStoredCredentialsHexEncoded(true);

        UserRealm realm = new UserRealm(userService);
        realm.setCredentialsMatcher(matcher);
        return realm;
    }
}
