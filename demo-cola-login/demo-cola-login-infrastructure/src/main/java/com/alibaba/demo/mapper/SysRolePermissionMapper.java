package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.sysuser.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    @Select("select * from sys_role_permission where role_id = #{roleId}")
    List<SysRolePermission> getListByRoleId(@Param("roleId") Long roleId);

    @Select("select * from sys_role_permission where role_id in (${roleIds})")
    List<SysRolePermission> getListByRoleIds(@Param("roleIds") String roleIds);
}
