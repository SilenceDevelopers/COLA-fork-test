package com.alibaba.demo.gateway.impl;

import com.alibaba.demo.constants.RedisConstants;
import com.alibaba.demo.utils.SeckillTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.alibaba.demo.constants.RedisConstants.TOKEN_SET_KEY;

@Service
public class SeckillService {

    @Autowired
    private TokenBucketService tokenBucketService;

    @Autowired
    private SeckillTokenUtil tokenUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String TOKEN_USED_KEY_PREFIX = "seckill:token_used:";


    /**
     * 秒杀入口：限流 + 发令牌
     */
    public String tryGetSeckillToken(Long userId, Long productId) {
        boolean allowed = tokenBucketService.tryConsumeToken();
        if (!allowed) {
            return null; // 限流拒绝
        }
        // 可加业务校验：用户是否登录、是否重复抢购等
        return tokenUtil.generateToken(userId, productId);
    }

    /**
     * 秒杀执行接口：校验令牌 + 防重放
     */
    public boolean doSeckill(String token) {
        try {
            Claims claims = tokenUtil.validateToken(token);
            String redisKey = TOKEN_USED_KEY_PREFIX + token;
            Boolean used = redisTemplate.hasKey(redisKey);
            if (Boolean.TRUE.equals(used)) {
                return false; // 令牌已用，防止重复秒杀
            }

            // TODO: 执行业务：扣库存、生成订单

            redisTemplate.opsForValue().set(redisKey, "1", 10, TimeUnit.MINUTES);
            return true;
        } catch (JwtException e) {
            return false; // 令牌校验失败
        }
    }

    public boolean isDuplicate(String token) {
        // 布隆过滤器判断（可能误判）
        Boolean mayExist = (Boolean) redisTemplate.execute(
                connection -> connection.execute("BF.EXISTS", RedisConstants.TOKEN_BLOOM_KEY.getBytes(), token.getBytes()),
                true
        );

        if (Boolean.FALSE.equals(mayExist)) {
            return false;
        }

        // 精准 Redis Set 判断
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(TOKEN_SET_KEY, token));
    }

    public void recordToken(String token) {
        // TODO: 执行业务

        tokenBucketService.recordToken(token);
    }
}
