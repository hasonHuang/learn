package com.hason.permission;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 支持通配符的位权限
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/13
 */
public class BitAndWildcardPermissionResolver implements PermissionResolver {

    @Override
    public Permission resolvePermission(String permissionString) {
        if (StringUtils.startsWith(permissionString, "+")) {
            // 如果字符串以 + 号开头，则返回位移方式的权限
            return new BitPermission(permissionString);
        }
        // 否则返回通配符权限
        return new WildcardPermission(permissionString);
    }

}
