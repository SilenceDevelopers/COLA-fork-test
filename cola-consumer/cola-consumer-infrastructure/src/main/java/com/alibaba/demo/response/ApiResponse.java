package com.alibaba.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private Integer code;
    private String msg;
    private Object data;

    public ApiResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
