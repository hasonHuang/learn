package com.hason.dao;

import com.hason.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色 DAO
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
@Mapper
public interface RoleDao {


    boolean createRole(Role role);

    void deleteRole(@Param("roleId") Long roleId);

    void correlationPermissions(@Param("roleId") Long roleId, @Param("permissionIds") Long... permissionIds);

    void uncorrelationPermissions(@Param("roleId") Long roleId, @Param("permissionIds") Long... permissionIds);

}
