package com.hason.realm;

import net.sf.ehcache.Ehcache;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 有重试次数限制的密码匹配，基于 HashedCredentialsMatcher 实现
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/26
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    /** 记录重试次数 */
//    private Ehcache ehcache;
    private static Map<String, AtomicInteger> counter;
    /** 最大的重试次数 */
    private int maxLimit = 5;

    public RetryLimitHashedCredentialsMatcher() {
        // 有时间的话可以优化，改为 Ehcache 或者 Redis 版本
        counter = new HashMap<>();
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        AtomicInteger num = counter.getOrDefault(username, new AtomicInteger(0));
        counter.put(username, num);
        if (num.incrementAndGet() > maxLimit) {
            // 重试次数过多
            throw new ExcessiveAttemptsException();
        }

        boolean match = super.doCredentialsMatch(token, info);
        if (match) {
            // 密码正确时，重试次数归零
            counter.remove(username);
        }
        return match;
    }
}
