package com.alibaba.demo.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.demo.response.ApiResponse;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class CustomBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        // 统一处理所有阻塞异常（限流、降级、系统保护）
        response.setStatus(429); // Too Many Requests
        response.setContentType("application/json;charset=utf-8");

        String message = "请求被限制，请稍后重试";
        if (e instanceof DegradeException) {
            message = "服务暂时不可用，请稍后重试";
        }
        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(new ApiResponse(429, message)));
        writer.flush();
    }
}
