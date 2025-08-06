package com.alibaba.demo.config;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
public class ZKConfig {

    private String address;

    private Integer baseSleepTimeMs;

    private Integer maxRetries;

    @Bean
    public CuratorFramework curatorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs,maxRetries);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(address,retryPolicy);
        curatorFramework.start();
        return curatorFramework;
    }
}
