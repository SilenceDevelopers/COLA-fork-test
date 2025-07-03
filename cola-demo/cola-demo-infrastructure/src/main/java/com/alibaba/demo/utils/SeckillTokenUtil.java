package com.alibaba.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class SeckillTokenUtil {

    private Key secretKey;
    private static final long EXPIRATION_MS = 5 * 60 * 1000;

    @PostConstruct
    public void init() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(Long userId, Long productId) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + EXPIRATION_MS);
        return Jwts.builder()
                .setSubject("seckill_token")
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .claim("userId", userId)
                .claim("productId", productId)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 校验令牌，返回Claims
     */
    public Claims validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
