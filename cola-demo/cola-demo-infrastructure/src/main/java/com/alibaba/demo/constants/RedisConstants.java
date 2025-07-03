package com.alibaba.demo.constants;

public class RedisConstants {
    public static final String TOKEN_BUCKET_KEY = "seckill_token_bucket";
    public static final int MAX_TOKENS = 100;         // 秒杀入口最大并发令牌数（可调）
    public static final int REFILL_TOKENS = 10;       // 每次补充令牌数
    public static final int REFILL_INTERVAL = 1000;   // 补充间隔ms
    public static final String TOKEN_BLOOM_KEY = "seckill:bloom";
    public static final String TOKEN_SET_KEY = "seckill:used:set";
}
