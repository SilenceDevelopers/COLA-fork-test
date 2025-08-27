package com.alibaba.demo.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.demo.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DegradeException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleDegradeException(HttpServletRequest request, DegradeException exception){
        return ResponseEntity.status(503).body(new ApiResponse(503,"服务降级中，请稍后重试"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleException(HttpServletRequest request,Exception exception){
        return ResponseEntity.status(500).body(new ApiResponse(500,"系统异常："+exception.getMessage()));
    }
}
