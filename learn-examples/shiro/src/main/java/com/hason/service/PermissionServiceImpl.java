package com.hason.service;

import com.hason.dao.PermissionDao;
import com.hason.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限接口
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public Permission createPermission(Permission permission) {
        permissionDao.insert(permission);
        return permission;
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionDao.delete(permissionId);
    }

}
