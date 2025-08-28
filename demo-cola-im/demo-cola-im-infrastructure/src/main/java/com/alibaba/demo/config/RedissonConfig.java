package com.alibaba.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        if (redisProperties.getCluster() != null) {
            List<String> nodes = redisProperties.getCluster().getNodes();
            config.useClusterServers().addNodeAddress(nodes.stream().map(n -> "redis://" + n).toArray(String[]::new)).setPassword(redisProperties.getPassword());
        } else {
            config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort()).setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }
}
