package com.hason.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义的角色权限解析器
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/13
 */
public class MyRolePermissionResolver implements RolePermissionResolver {
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        // 如果用户拥有 role1 角色，那么返回 "menu:*" 的权限
        if ("role1".equals(roleString)) {
            return Collections.singletonList(new WildcardPermission("menu:*"));
        }
        return null;
    }
}
