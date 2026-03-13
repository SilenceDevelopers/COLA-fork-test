package com.alibaba.demo.response;

import lombok.Data;

@Data
public class FallbackResponse {

    private Integer code;
    private String message;

    public FallbackResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
