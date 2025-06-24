package com.alibaba.demo.domain.sysuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole {

    private Long id;

    private Long userId;

    private Long roleId;
}
