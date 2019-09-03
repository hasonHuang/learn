package com.hason.springcache.service;

import com.hason.springcache.entity.UserInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户业务类
 *
 * @author Hason
 */
@Service
public class UserService {

    /**
     * 模拟数据库
     */
    private Map<Long, UserInfo> db = new HashMap<>();

    {
        db.put(1L, new UserInfo(1L, "Tom"));
        db.put(2L, new UserInfo(2L, "Jack"));
        db.put(3L, new UserInfo(3L, "Amy"));
    }

    /**
     * 根据 ID 获取用户信息
     */
    @Cacheable(cacheManager = "cacheManager", value = "user")
    public UserInfo get(Long id) {
        return db.get(id);
    }

}
