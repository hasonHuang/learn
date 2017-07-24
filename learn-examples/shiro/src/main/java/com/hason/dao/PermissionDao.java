package com.hason.dao;

import com.hason.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限 dao
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
@Mapper
public interface PermissionDao {

    boolean insert(Permission permission);

    boolean delete(Long id);
}
