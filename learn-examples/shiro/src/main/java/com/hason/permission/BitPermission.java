package com.hason.permission;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.Permission;

/**
 * BitPermission 用于实现位移方式的权限，如规则是：
 *
 * 权限字符串格式：+资源字符串+权限位+实例 ID；以+开头 中间通过+分割；权限：0 表示
 * 所有权限；1 新增（二进制：0001）、2 修改（二进制：0010）、4 删除（二进制：0100）、
 * 8 查看（二进制：1000）；如 +user+10 表示对资源 user 拥有修改/查看权限
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/13
 */
public class BitPermission implements Permission {

    private String resourceIdentify;
    private int permissionBit;
    private String instanceId;

    public BitPermission(String permissionString) {
        String[] arr = permissionString.split("\\+");
        if (arr.length > 1) {
            resourceIdentify = arr[1];
        }
        if (StringUtils.isEmpty(resourceIdentify)) {
            resourceIdentify = "*";
        }
        if (arr.length > 2) {
            permissionBit = Integer.valueOf(arr[2]);
        }
        if (arr.length > 3) {
            instanceId = arr[3];
        }
        if (StringUtils.isEmpty(instanceId)) {
            instanceId = "*";
        }
    }

    // 判断权限匹配，是否匹配权限 p
    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof BitPermission)) {
            return false;
        }
        BitPermission other = (BitPermission) p;
        // 如果是拥有全部资源或该资源，则通过
        if(!("*".equals(this.resourceIdentify) ||
                this.resourceIdentify.equals(other.resourceIdentify))) {
            return false;
        }
        // 如果拥有全部权限或该权限，则通过（ 只能判断拥有其中一些权限，如：1010 & 1110）
        if(!(this.permissionBit == 0 || (this.permissionBit & other.permissionBit) != 0)) {
            return false;
        }
        // 如果可操作全部实例或该实例，则通过
        if(!("*".equals(this.instanceId) || this.instanceId.equals(other.instanceId))) {
            return false;
        }
        return true;
    }
}
