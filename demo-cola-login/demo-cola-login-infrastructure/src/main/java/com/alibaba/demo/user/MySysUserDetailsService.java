package com.alibaba.demo.user;

import com.alibaba.demo.domain.sysuser.*;
import com.alibaba.demo.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MySysUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException  {
        SysUser sysUser = sysUserMapper.getByUsername(username);
        if (!sysUser.getUsername().equals(username)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        MySysUserDetails mySysUserDetails = new MySysUserDetails(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), null, null);
        List<SysUserRole> userRoles = sysUserRoleMapper.getListByUserId(mySysUserDetails.getId());
        List<Long> roleIds = userRoles.stream().map(SysUserRole::getId).toList();
        String roleIdsStr = roleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        List<SysRole> sysRoles = sysRoleMapper.getListByIds(roleIdsStr);
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.getListByRoleIds(roleIdsStr);
        List<SysPermission> sysPermissions = sysPermissionMapper.getListByIds(rolePermissions.stream().map(SysRolePermission::getPermissionId).map(String::valueOf).collect(Collectors.joining(",")));
        mySysUserDetails.setSysRoles(sysRoles);
        mySysUserDetails.setPermissions(sysPermissions);
        return mySysUserDetails;
    }

}
