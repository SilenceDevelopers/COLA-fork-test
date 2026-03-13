package com.alibaba.demo.response;

import lombok.Data;

@Data
public class ApiResponse <T>{
    private int code;        // 状态码，如 200 表示成功
    private String message;   // 提示信息
    private T data;           // 实际返回的数据（在分页场景中就是 PageResult 对象）
    private boolean success;  // 是否成功（可选，通常可由 code 判断）

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(200);
        resp.setMessage("操作成功");
        resp.setData(data);
        resp.setSuccess(true);
        return resp;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(500);
        resp.setMessage(message);
        resp.setSuccess(false);
        return resp;
    }
}
