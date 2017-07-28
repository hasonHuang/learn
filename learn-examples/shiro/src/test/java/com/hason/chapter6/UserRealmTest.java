package com.hason.chapter6;

import com.hason.BaseTest;
import org.junit.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 用户源测试，在BaseTest里创建用户
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/28
 */
public class UserRealmTest extends BaseTest {

    @Test
    public void testLogin() {
        login(u1.getUsername(), password);
    }

    @Test(expected = UnknownAccountException.class)
    public void testLoginFailWithUnknownUsername() {
        login( u1.getUsername() + "1", password);
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void testLoginFailWithErrorPassowrd() {
        login( u1.getUsername(), password + "1");
    }

    @Test(expected = LockedAccountException.class)
    public void testLoginFailWithLocked() {
        login( u4.getUsername(), password + "1");
    }

    @Test(expected = ExcessiveAttemptsException.class)
    public void testLoginFailWithLimitRetryCount() {
        for(int i = 1; i <= 5; i++) {
            try {
                login( u3.getUsername(), password + "1");
            } catch (Exception e) {/*ignore*/}
        }
        login( u3.getUsername(), password + "1");

        //需要清空缓存，否则后续的执行就会遇到问题(或者使用一个全新账户测试)
    }


    @Test
    public void testHasRole() {
        login( u1.getUsername(), password );
        Assert.assertTrue(subject().hasRole("admin"));
    }

    @Test
    public void testNoRole() {
        login( u2.getUsername(), password);
        Assert.assertFalse(subject().hasRole("admin"));
    }

    @Test
    public void testHasPermission() {
        login( u1.getUsername(), password);
        Assert.assertTrue(subject().isPermittedAll("user:create", "menu:create"));
    }

    @Test
    public void testNoPermission() {
        login( u2.getUsername(), password);
        Assert.assertFalse(subject().isPermitted("user:create"));
    }

    private void login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password));
    }

    private Subject subject() {
        return SecurityUtils.getSubject();
    }
}
