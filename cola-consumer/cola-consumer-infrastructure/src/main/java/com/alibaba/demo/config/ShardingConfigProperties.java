package com.alibaba.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sharding")
@Data
public class ShardingConfigProperties {

    private int databaseCount = 16;
    private int tableCount = 16;
}
