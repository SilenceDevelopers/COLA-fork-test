package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.sysuser.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Select("select * from sys_permission where id = #{id}")
    SysPermission getById(@Param("id") Long id);

    @Select("select * from sys_permission where id in (${ids})")
    List<SysPermission> getListByIds(@Param("ids") String ids);
}
