package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.sysuser.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("select * from sys_user_role where user_id = #{userId}")
    List<SysUserRole> getListByUserId(@Param("userId") Long userId);
}
