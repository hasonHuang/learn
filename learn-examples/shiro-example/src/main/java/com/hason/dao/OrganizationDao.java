package com.hason.dao;

import com.hason.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构 dao
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/24
 */
@Mapper
public interface OrganizationDao {

    Organization createOrganization(Organization organization);
    Organization updateOrganization(Organization organization);
    void deleteOrganization(@Param("id") Long organizationId);

    Organization findOne(@Param("id") Long organizationId);
    List<Organization> findAll();

    List<Organization> findAllWithExclude(@Param("id") long id, @Param("makeSelfAsParentIds") String makeSelfAsParentIds);

//    void move(Organization source, Organization target);
}
