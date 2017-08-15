package com.hason.realm;

import com.hason.entity.User;
import com.hason.service.UserService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义的用户来源
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/13
 */
public class UserRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(userService.findRoles(username));
        info.setStringPermissions(userService.findPermissions(username));

        debug(String.format("获取[%s]授权信息：%s", username, ToStringBuilder.reflectionToString(info)));

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        if (user == null) {
            debug("找不到用户账号：" + username);
            throw new UnknownAccountException();
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            debug("用户账号已锁定：" + username);
            throw new LockedAccountException();
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                        username,
                        user.getPassword(),
                        ByteSource.Util.bytes(user.getCredentialsSalt()),
                        getName()
                );

        debug("获取认证信息：" + info);

        return info;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }
}
