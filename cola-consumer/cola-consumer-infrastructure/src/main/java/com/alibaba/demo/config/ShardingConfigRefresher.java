package com.alibaba.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class ShardingConfigRefresher {

    @Autowired
    private ShardingRouter shardingRouter;

    @RefreshScope
    @ConfigurationProperties(prefix = "sharding")
    public void refreshShardingConfig(ShardingConfigProperties config) {
        shardingRouter.updateShardingConfig(config.getDatabaseCount(), config.getTableCount());
    }
}
