package com.alibaba.demo.domain.sysuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission {

    private Long id;

    private Long roleId;

    private Long permissionId;
}
