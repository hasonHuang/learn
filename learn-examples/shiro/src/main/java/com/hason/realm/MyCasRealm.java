package com.hason.realm;

import com.hason.config.CasConfig;
import com.hason.entity.User;
import com.hason.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 自定义实现的 CAS Realm
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/11
 */
public class MyCasRealm extends CasRealm {

    private static final Logger logger = LoggerFactory.getLogger(MyCasRealm.class);

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initUrl() {
        // CAS 服务端地址前缀
        setCasServerUrlPrefix(CasConfig.casServerUrlPrefix);
        // 客户端回调地址
        setCasService(CasConfig.shiroServerUrlPrefix + CasConfig.casFilterUrlPattern);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(userService.findRoles(username));
        info.setStringPermissions(userService.findPermissions(username));
        return info;
//        return super.doGetAuthorizationInfo(principals);
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
//        return super.doGetAuthenticationInfo(token);
    }

}
