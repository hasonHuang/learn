package com.hason;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * 测试 RBAC (Role-based Access Control) 基于角色的访问控制
 *
 * 隐式角色：即直接通过角色来验证用户有没有操作权限
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/12
 */
public class RoleTest {

    @Test
    public void testHasRole() {
        try {
            login("classpath:chapter1-3/shiro-role.ini", "hason", "123");
        } catch (AuthenticationException e) {
            System.out.println("验证失败");
        }

        Subject subject = SecurityUtils.getSubject();
        // 判断是否拥有的角色： role1
        Assert.assertTrue("没有role1", subject.hasRole("role1"));
        // 判断是否拥有的角色： role1 and role2
        Assert.assertTrue("没有role1 and role2", subject.hasAllRoles(Arrays.asList("role1", "role2")));
        // 判断是否拥有的角色： role1 and role2 and !role3
        boolean[] hasResult = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertTrue("没有role1", hasResult[0]);
        Assert.assertTrue("没有role2", hasResult[1]);
        Assert.assertTrue("没有role3", hasResult[2]);

    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:chapter1-3/shiro-role.ini", "wang", "123");
        //断言拥有角色：role1
        subject().checkRole("role1");
        //断言拥有角色：role1 and role3 失败抛出异常
        subject().checkRoles("role1", "role3");
    }

    private Subject subject() {
        return SecurityUtils.getSubject();
    }

    private void login(String resources, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:chapter1-3/shiro-role.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password));
    }
}
