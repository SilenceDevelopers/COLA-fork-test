package com.alibaba.demo.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConfigurationProperties(prefix = "snowflake")
@Configuration
@Data
public class IdGeneratorSnowflake {

    private long workerId;  //第几号机房
    private long datacenterId;  //第几号机器
    private Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

    @Bean
    public Snowflake snowflake() {
        log.info("雪花算法workerId:" + workerId + ",datacenterId:" + datacenterId);
        return new Snowflake(workerId, datacenterId);
    }
}
