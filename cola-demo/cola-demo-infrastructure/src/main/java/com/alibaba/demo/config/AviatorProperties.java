package com.alibaba.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "aviator")
@Component
public class AviatorProperties {

    private boolean cacheEnabled = true;
    private int cacheSize = 1000;
    private long timeoutMs = 3000;
    private List<String> functions = new ArrayList<>();
}
