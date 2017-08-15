package com.hason.config;

import com.hason.realm.RetryLimitHashedCredentialsMatcher;
import com.hason.realm.UserRealm;
import com.hason.service.UserService;
import com.hason.util.PasswordHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Base64;
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

    /*---------------------------------------------
    |          配置 Shiro 的 Web 过滤器              |
    ==============================================*/

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        //Shiro的核心安全接口,这个属性是必须的
        factoryBean.setSecurityManager(securityManager);
        // 要求登录时的链接(可根据项目的URL进行替换),非必须的属性,默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        factoryBean.setLoginUrl("/loginPage");
        factoryBean.setLoginUrl(CasConfig.loginUrl);
        // 登录成功后要跳转的连接,逻辑也可以自定义，例如返回上次请求的页面
        factoryBean.setSuccessUrl("/loginSuccess");
        // 用户访问未对其授权的资源时,所显示的连接
        factoryBean.setUnauthorizedUrl("/403");


        /*
         * 定义shiro过滤链  Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         *
         * DefaultFilterChainManager 默认会添加 org.apache.shiro.web.filter.mgt.DefaultFilter 中声明的拦截器
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
        filterChainDefinitionMap.put("/ssl", "ssl,authc");

        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边; 这是一个坑呢，一不小心代码就不好使了;
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        // user: 表示访问该地址的用户是身份验证通过或RememberMe 登录的都可以
        filterChainDefinitionMap.put("/**", "user");

        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        // 添加 Filter
        Map<String, Filter> filterMap = new LinkedHashMap<>();
//        filterMap.put("ssl", sslFilter());
//        filterMap.put("authc", formAuthenticationFilter());
        filterMap.put("casFilter", casFilter);
        factoryBean.setFilters(filterMap);

        return factoryBean;
    }


    /*---------------------------------------------
    |            配置 Filter 过滤器                 |
    ==============================================*/
    // 支持 SSL
//    @Bean
//    public SslFilter sslFilter() {
//        SslFilter sslFilter = new SslFilter();
//        sslFilter.setPort(8443);
//        return sslFilter;
//    }

//    @Bean
//    public FormAuthenticationFilter formAuthenticationFilter() {
//        FormAuthenticationFilter filter = new FormAuthenticationFilter();
//        // 记住我的请求参数名，boolean 类型
//        filter.setRememberMeParam("rememberMe");
//        return filter;
//    }


    /*---------------------------------------------
    |            配置RememberMe                    |
    ==============================================*/

    @Bean
    public CookieRememberMeManager rememberMeManager(){
//        logger.info("注入Shiro的记住我(CookieRememberMeManager)管理器-->rememberMeManager", CookieRememberMeManager.class);
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
        //KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //SecretKey deskey = keygen.generateKey();
        //System.out.println(Base64.encodeToString(deskey.getEncoded()));
        byte[] cipherKey = Base64.getDecoder().decode("wGiHplamyXlVB11UXWol8g==");
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        simpleCookie.setHttpOnly(true);
        //记住我cookie生效时间,默认30天 ,单位秒：60 * 60 * 24 * 30
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }


    /*---------------------------------------------
    |               开启注解功能                     |
    ==============================================*/

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
//    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    
    /*---------------------------------------------
    |            配置SecurityManager               |
    ==============================================*/

//    @Bean
//    public SecurityManager securityManager(UserRealm realm, SessionManager sessionManager) {
//        // 必须使用 DefaultWebSecurityManager，否则启动时报错 SecurityManager 没有实现 WebSecurityManager 接口
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(realm);                            // 设置数据源
//        securityManager.setSessionManager(sessionManager);          // 设置会话管理器
//        securityManager.setRememberMeManager(rememberMeManager());  // 设置RememberMe管理器
//
//        SecurityUtils.setSecurityManager(securityManager);
//        // <!-- 相当于调用 SecurityUtils.setSecurityManager(securityManager) -->
//        // <bean class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
//        // <property name="staticMethod"
//        //         value="org.apache.shiro.SecurityUtils.setSecurityManager"/>
//        // <property name="arguments" ref="securityManager"/>
//        // </bean>
//
//        return securityManager;
//    }

    @Bean
    public UserRealm userRealm(UserService userService, CredentialsMatcher matcher) {
        UserRealm realm = new UserRealm(userService);
        realm.setCredentialsMatcher(matcher);
        return realm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher(PasswordHelper passwordHelper) {
        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        // 此处要与加密时的次数一致，否者一直无法通过验证
        matcher.setHashIterations(passwordHelper.getHashIterations());
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }
}
