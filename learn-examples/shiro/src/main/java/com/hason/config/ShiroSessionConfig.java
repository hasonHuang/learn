package com.hason.config;

import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Shiro 会话管理器
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/2
 */
@Configuration
public class ShiroSessionConfig {

    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        // 用于 Web 环境的会话管理器，自己维护着会话，不依赖 Servlet 容器
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(20 * 60 * 1000);     // 全局过期时间 20 分钟
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    // 会话存储/持久化
    // 如果需要自定义 SessionDAO（如 Redis 存储会话），继承 CachingSessionDAO 即可
    @Bean
    public SessionDAO sessionDAO() {
        return new EnterpriseCacheSessionDAO();
    }

}
