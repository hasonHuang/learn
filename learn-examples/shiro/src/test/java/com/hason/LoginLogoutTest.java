package com.hason;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Hason on 2017/7/4.
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
}
