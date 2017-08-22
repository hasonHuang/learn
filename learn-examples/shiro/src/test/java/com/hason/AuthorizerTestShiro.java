package com.hason;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试授权
 *
 * 如果 Realm 进行授权的话，应该继承 AuthorizingRealm，其流程是：
 *
 * 1.1、如果调用 hasRole*，则直接获取 AuthorizationInfo.getRoles()与传入的角色比较即可；
 * 1.2、如果调用如 isPermitted(“user:view”)，首先通过 PermissionResolver 将权限字符串
 * 转换成相应的 Permission 实例，默认使用 WildcardPermissionResolver，即转换为通配符的 WildcardPermission；
 *
 * 2 、 通 过 AuthorizationInfo.getObjectPermissions() 得 到 Permission 实 例 集 合 ； 通 过
 * AuthorizationInfo. getStringPermissions()得到字符串集合并通过 PermissionResolver 解析为
 * Permission 实例；然后获取用户的角色，并通过 RolePermissionResolver 解析角色对应的权
 * 限集合（默认没有实现，可以自己提供）；
 *
 * 3、接着调用 Permission. implies(Permission p)逐个与传入的权限比较，如果有匹配的则返回
 * true，否则 false。
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/13
 */
public class AuthorizerTestShiro extends ShiroBaseTest {

    @Test
    public void testIsPermitted() {
        login("classpath:chapter1-3/shiro-authorizer.ini", "com.hason", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }

    // 另外我们可以使用 JdbcRealm，需要做的操作如下：
    // 1、执行 sql/ shiro-init-data.sql 插入相关的权限数据；
    // 2、使用 shiro-jdbc-authorizer.ini 配置文件，需要设置 jdbcRealm.permissionsLookupEnabled
    //    为 true 来开启权限查询。
    @Test
    public void testIsPermitted2() {
        login("classpath:chapter1-3/shiro-jdbc-authorizer.ini", "com.hason", "123");
        //判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        //通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }


}
