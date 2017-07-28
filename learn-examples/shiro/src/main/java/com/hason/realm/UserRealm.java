package com.hason.realm;

import com.hason.entity.User;
import com.hason.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户身份来源
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/28
 */
public class UserRealm extends AuthorizingRealm {

    private UserService userService;

    public UserRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(userService.findRoles(username));
        info.setStringPermissions(userService.findPermissions(username));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UnknownAccountException();    // 没有找到账号
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException();     // 账号已锁定
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName());
        return info;
    }
}
