package com.hason.realm;

import com.hason.permission.BitPermission;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 继承 AuthorizingRealm 而不是实现 Realm 接口；推荐使用 AuthorizingRealm
 * 这种方式的好处是当只需要身份验证时只需要获取身份验证信息而不需要获取授权信息。
 * 对于 AuthenticationInfo 和 AuthorizationInfo 请参考其 Javadoc 获取相关接口信息
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/13
 */
public class MyAuthorizingRealm extends AuthorizingRealm {

    // 根据用户身份获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("role1");
        authorizationInfo.addRole("role2");
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        authorizationInfo.addStringPermission("+user2+10");
        authorizationInfo.addStringPermission("user2:*");
        return authorizationInfo;
    }

    // 获取身份验证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
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
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
