package com.alibaba.demo.gateway.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class TokenBucketService {

    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> tokenBucketScript;

    private static final String TOKEN_BUCKET_KEY = "seckill_token_bucket";
    private static final int MAX_TOKENS = 100;         // 秒杀入口最大并发令牌数（可调）
    private static final int REFILL_TOKENS = 10;       // 每次补充令牌数
    private static final int REFILL_INTERVAL = 1000;   // 补充间隔ms

    @PostConstruct
    public void init() {
        try {
            String scriptText = new BufferedReader(new InputStreamReader(
                    new ClassPathResource("lua/token_bucket.lua").getInputStream(),
                    StandardCharsets.UTF_8
            )).lines().collect(Collectors.joining("\n"));

            tokenBucketScript = new DefaultRedisScript<>();
            tokenBucketScript.setScriptText(scriptText);
            tokenBucketScript.setResultType(Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Lua script", e);
        }
    }

    public boolean tryConsumeToken() {
        Long now = Instant.now().toEpochMilli();
        Long result = (Long) redisTemplate.execute(tokenBucketScript,
                Collections.singletonList(TOKEN_BUCKET_KEY),
                String.valueOf(MAX_TOKENS),
                String.valueOf(REFILL_TOKENS),
                String.valueOf(REFILL_INTERVAL),
                String.valueOf(now));
        return result != null && result == 1;
    }
}
