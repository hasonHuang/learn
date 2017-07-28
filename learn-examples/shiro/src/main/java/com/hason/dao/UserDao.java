package com.hason.dao;

import com.hason.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 用户 dao
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/26
 */
@Mapper
public interface UserDao {

    // 用户 增删改查
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(@Param("id") Long userId);
    User findOne(@Param("id") Long userId);
    User findByUsername(@Param("username") String username);

    // 关联用户角色
    boolean correlationRoles(@Param("userId") Long userId, @Param("roleIds") Long... roleIds);
    int uncorrelationRoles(@Param("userId") Long userId, @Param("roleIds") Long... roleIds);

    // 查找用户角色
    Set<String> findRoles(@Param("username") String username);
    Set<String> findPermissions(@Param("username") String username);
}
