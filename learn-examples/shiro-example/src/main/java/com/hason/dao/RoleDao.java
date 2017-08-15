package com.hason.dao;

import com.hason.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 DAO
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
@Mapper
public interface RoleDao {

    Role createRole(Role role);
    Role updateRole(Role role);
    void deleteRole(@Param("id") Long roleId);

    Role findOne(@Param("id") Long roleId);
    List<Role> findAll();
}
