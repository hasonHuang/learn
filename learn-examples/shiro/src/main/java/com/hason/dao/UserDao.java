package com.hason.dao;

import com.hason.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 用户 dao
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/26
 */
public interface UserDao {

    // 用户 增删改查
    User createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Long userId);
    User findOne(Long userId);
    User findByUsername(String username);

    // 关联用户角色
    boolean correlationRoles(@Param("userId") Long userId, @Param("roleIds") Long... roleIds);
    int uncorrelationRoles(@Param("userId") Long userId, @Param("roleIds") Long... roleIds);

    // 查找用户角色
    Set<String> findRoles(@Param("username") String username);
    Set<String> findPermissions(@Param("username") String username);
}
