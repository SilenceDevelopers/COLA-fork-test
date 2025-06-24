package com.alibaba.demo.user;

import com.alibaba.demo.domain.sysuser.SysPermission;
import com.alibaba.demo.domain.sysuser.SysRole;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySysUserDetails implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private List<SysRole> sysRoles;

    private List<SysPermission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = Lists.newArrayList();
        sysRoles.forEach(role -> list.add(new SimpleGrantedAuthority(role.getRoleName())));
        permissions.forEach(permission -> list.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
