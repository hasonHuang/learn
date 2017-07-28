package com.hason.config;

import com.hason.realm.RetryLimitHashedCredentialsMatcher;
import com.hason.realm.UserRealm;
import com.hason.service.UserService;
import com.hason.util.PasswordHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置用户源
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/28
 */
@Configuration
public class RealmConfig {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordHelper passwordHelper;

    @Bean
    public SecurityManager getSecurityManager() {

        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        // 此处要与加密时的次数一致，否者一直无法通过验证
        matcher.setHashIterations(passwordHelper.getHashIterations());
        matcher.setStoredCredentialsHexEncoded(true);

        UserRealm realm = new UserRealm(userService);
        realm.setCredentialsMatcher(matcher);

        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);

        return SecurityUtils.getSecurityManager();
    }
}
