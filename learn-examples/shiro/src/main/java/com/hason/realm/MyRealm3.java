package com.hason.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * 简单地实现自定义 Realm
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/5
 */
public class MyRealm3 implements Realm {
    @Override
    public String getName() {
        return "myRealm3";   // 返回一个唯一的 realm 的名字
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // 仅支持 UsernamePasswordToken 类型的 Token
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户名
        String username = (String) token.getPrincipal();
        // 获取密码
        String password = new String((char[]) token.getCredentials());
        if (!"com.hason".equals(username)) {
            throw new UnknownAccountException();
        }
        if (!"123".equals(password)) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(username + "@163.com", password, getName());
    }
}
