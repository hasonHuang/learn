package com.hason.dao;

import com.hason.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    User createUser(User user);
    User updateUser(User user);
    void deleteUser(@Param("id") Long userId);

    User findOne(@Param("id") Long userId);

    List<User> findAll();

    User findByUsername(@Param("username") String username);

}
