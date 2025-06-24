package com.alibaba.demo.domain.sysuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {

    private Long id;

    private String permissionName;
}
