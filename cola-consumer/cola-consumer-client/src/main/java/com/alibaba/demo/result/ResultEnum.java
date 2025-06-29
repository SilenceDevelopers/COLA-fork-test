package com.alibaba.demo.result;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(200,"请求成功"),

    FAILED_BAD_REQUEST(400, "错误的请求"),
    FAILED_UNAUTHORIZED(401, "未登录"),
    FAILED_FORBIDDEN(403, "未经授权"),

    FAILED(500, "服务器内部错误");

    public final int code;
    public final String message;

    ResultEnum(int code, String message){
        this.code=code;
        this.message=message;
    }
}
