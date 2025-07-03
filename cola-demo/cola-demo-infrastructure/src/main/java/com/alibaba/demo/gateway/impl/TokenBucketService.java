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
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class TokenBucketService {

    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> tokenBucketScript;
    private DefaultRedisScript<Long> recordBucketScript;

    private static final String TOKEN_BUCKET_KEY = "seckill_token_bucket";
    private static final int MAX_TOKENS = 100;         // 秒杀入口最大并发令牌数（可调）
    private static final int REFILL_TOKENS = 10;       // 每次补充令牌数
    private static final int REFILL_INTERVAL = 1000;   // 补充间隔ms
    private static final String TOKEN_BLOOM_KEY = "seckill:bloom";
    private static final String TOKEN_SET_KEY = "seckill:used:set";

    @PostConstruct
    public void init() {
        this.recordBucketScript = loadLuaScript("lua/record_token.lua");
        this.tokenBucketScript = loadLuaScript("lua/token_bucket.lua");
    }

    private DefaultRedisScript<Long> loadLuaScript(String path) {
        try {
            String scriptText = new BufferedReader(new InputStreamReader(
                    new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(scriptText);
            script.setResultType(Long.class);
            return script;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Lua script: " + path, e);
        }
    }

    public boolean tryConsumeToken() {
        Long now = Instant.now().toEpochMilli();
        Long result = (Long) redisTemplate.execute(tokenBucketScript,
                Collections.singletonList(TOKEN_BUCKET_KEY),
                MAX_TOKENS,
                REFILL_TOKENS,
                REFILL_INTERVAL,
                now);
        return result != null && result == 1;
    }

    public void recordToken(String token) {
        redisTemplate.execute(
                recordBucketScript,
                Arrays.asList(TOKEN_BLOOM_KEY, TOKEN_SET_KEY),
                token
        );
    }
}
