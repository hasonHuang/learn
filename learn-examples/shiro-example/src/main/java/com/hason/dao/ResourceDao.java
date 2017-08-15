package com.hason.dao;

import com.hason.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/13
 */
@Mapper
public interface ResourceDao {

    Resource createResource(Resource resource);
    Resource updateResource(Resource resource);
    void deleteResource(@Param("id") Long resourceId);

    Resource findOne(@Param("id") Long resourceId);
    List<Resource> findAll();
}
