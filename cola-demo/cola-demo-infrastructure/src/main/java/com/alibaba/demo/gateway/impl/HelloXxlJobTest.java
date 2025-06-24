package com.alibaba.demo.gateway.impl;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloXxlJobTest {

    @Value("${server.port}")
    private String port;

    @XxlJob("demoJobHandler")
    public void echo() {
        System.out.println("Hello, XXL-JOB" + port);
    }
}
