package com.hason.chapter4;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * 使用 Java 代码配置 Shiro
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/16
 */
public class JavaConfigCreateTest {

    // 该方法的 Java 代码配置等价于 chapter4/shiro-config.ini
    @Test
    public void testJavaConfig() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        // 设置 authenticator
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // 设置认证策略
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);

        // 设置 authorizer
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);

        // 设置 Realm
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/shiro");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        JdbcRealm realm = new JdbcRealm();
        realm.setDataSource(dataSource);
        realm.setPermissionsLookupEnabled(true);    // 开启权限查询
        securityManager.setRealm(realm);

        // 把 SecurityManager 设置到 SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);

        // 测试
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("hason", "123"));
        Assert.assertTrue(subject.isAuthenticated());
    }

    // 等同于 testJavaConfig() 的配置
    @Test
    public void testIniConfig() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:chapter4/shiro-config.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("hason", "123"));
        Assert.assertTrue(subject.isAuthenticated());
    }
}
