package com.hason;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * RBAC 新解 (Resource-based Access Control，基于资源的访问控制)
 *
 *
 * 显式角色：在程序中通过权限控制谁能访问某个资源，角色聚合一组权限集合
 *
 * 基于权限的访问控制，这种方式的一般规则是“资源标识符：操作”
 *
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/12
 */
public class PermissionTest {


    // Shiro 提供了 isPermitted 和 isPermittedAll 用于判断用户是否拥有某个权限或所有权限，也
    // 没有提供如 isPermittedAny 用于判断拥有某一个权限的接口。
    @Test
    public void testIsPermission() {
        login("classpath:shiro-permission.ini", "hason", "123");
        //判断拥有权限：user:create
        Assert.assertTrue("没有权限 user:create", subject().isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue("没有权限 user:create && user:update", subject().isPermittedAll("user:create", "user:update"));
        //判断没有权限：user:view
        Assert.assertFalse("没有权限 user:view", subject().isPermitted("user:view"));
    }

    // 测试字符串通配符权限
    // 可以使用规则 "user:create,update" 去验证权限 checkPermissions("user:update", "user:create")
    // 但不能使用规则 "user:update,user:create" 去验证权限 checkPermissions("user:update,create")
    // 不能使用“非通配符规则”去匹配 “通配符”权限，如果通配符规则包含通配符权限所需的权限，可以通过检验
    @Test
    public void testWildcardPermission() {
        login("classpath:shiro-permission.ini", "karen", "123");
        // 判断拥有权限：user:update && user:create
        subject().checkPermissions("user:update,create");
        // 判断拥有权限：user:update && user:create
        subject().checkPermissions("user:update", "user:create");
        // 不能使用规则 "user:create,user:delete" 去验证权限 checkPermissions("user:update,delete")
        subject().checkPermissions("user:update,delete");
    }


    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission() {
        login("classpath:shiro-permission.ini", "wang", "123");
        //断言拥有权限：user:create
        subject().checkPermission("user:create");
        //断言拥有权限：user:delete and user:update
        subject().checkPermissions("user:delete", "user:update");
        //断言拥有权限：user:view 失败抛出异常
        subject().checkPermissions("user:view");
    }

    private Subject subject() {
        return SecurityUtils.getSubject();
    }

    private void login(String resource, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(resource);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        subject().login(new UsernamePasswordToken(username, password));
    }
}
