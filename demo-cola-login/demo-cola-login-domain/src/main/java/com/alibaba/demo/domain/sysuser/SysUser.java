package com.alibaba.demo.domain.sysuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

    private Long id;

    private String username;

    private String password;
}
