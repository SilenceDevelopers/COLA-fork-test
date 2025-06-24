package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.sysuser.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select * from sys_role where id = #{id}")
    SysRole getById(@Param("id") Long id);

    @Select("select * from sys_role where id in (${ids})")
    List<SysRole> getListByIds(@Param("ids") String ids);
}
