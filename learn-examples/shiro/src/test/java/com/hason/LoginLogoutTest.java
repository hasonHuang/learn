package com.hason;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试 Shiro 的登陆认证
 *
 * SecurityManager 接口继承了 Authenticator，另外还有一个 ModularRealmAuthenticator 实现，
 * 其委托给多个 Realm 进行验证，验证规则通过 AuthenticationStrategy 接口指定，默认提供的实现：
 *
 * FirstSuccessfulStrategy：只要有一个 Realm 验证成功即可，只返回第一个 Realm 身份验证
 *      成功的认证信息，其他的忽略；
 * AtLeastOneSuccessfulStrategy： 只要有一个 Realm 验证成功即可， 和 FirstSuccessfulStrategy
 *      不同，返回所有 Realm 身份验证成功的认证信息；
 * AllSuccessfulStrategy：所有 Realm 验证成功才算成功，且返回所有 Realm 身份验证成功的
 *      认证信息，如果有一个失败就失败了。
 * ModularRealmAuthenticator 默认使用 AtLeastOneSuccessfulStrategy 策略。
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/12
 */
public class LoginLogoutTest {

    @Test
    public void testHelloWorld() {
        //1、获取 SecurityManager 工厂，此处使用 Ini 配置文件初始化 SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //2、得到 SecurityManager 实例 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到 Subject 及创建用户名/密码身份验证 Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("hason", "123");
        //4、登陆，即身份验证
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            // 5. 验证失败
            System.err.println("身份验证失败");
        }
        // 断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());
        System.out.println("验证成功");

        // 6. 退出登录
        subject.logout();
    }

    // 测试自定义的 Realm
    @Test
    public void testCustomRealm() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken("hason", "123"));
        } catch (UnknownAccountException e) {
            System.out.println("用户名错误");
        } catch (IncorrectCredentialsException e) {
            System.out.println("密码错误");
        } catch (AuthenticationException e) {
            System.err.println("身份验证失败");
        }

        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();
    }

    // 测试 JDBC 的 Realm
    @Test
    public void testJdbcRealm() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken("hason", "123"));
        } catch (AuthenticationException e) {
            System.out.println("验证失败");
        }

        Assert.assertEquals(true, subject.isAuthenticated());
        subject.logout();
    }

    // 测试 AllSuccessfulStrategy 成功
    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();
        // 获取一个身份集合，其包含了 Realm 验证成功的身份信息
        PrincipalCollection principals = subject.getPrincipals();
        Assert.assertEquals(2, principals.asList().size());
    }

    private void login(String configFile) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("hason", "123"));
    }
}
