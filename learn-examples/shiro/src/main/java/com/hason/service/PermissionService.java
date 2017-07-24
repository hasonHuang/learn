package com.hason.service;

import com.hason.entity.Permission;

/**
 * 权限接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
public interface PermissionService {

    Permission createPermission(Permission permission);

    void deletePermission(Long permissionId);
}
