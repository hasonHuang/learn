package com.hason.service;

import com.hason.entity.Role;

/**
 * 角色接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
public interface RoleService {

    Role createRole(Role role);

    void deleteRole(Long roleId);

    /**
     * 添加角色-权限之间关系
     */
    void correlationPermissions(Long roleId, Long... permissionIds);

    /**
     * 移除角色-权限之间关系
     */
    void uncorrelationPermissions(Long roleId, Long... permissionIds);
}
