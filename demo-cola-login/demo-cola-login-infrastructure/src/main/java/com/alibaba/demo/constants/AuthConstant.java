package com.alibaba.demo.constants;

public class AuthConstant {
    public static final String TOKEN_HEADER = "Ya-Auth-Token";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String REDIS_KEY_AUTH_TOKEN = "auth:token:";
    public static final String REDIS_KEY_AUTH_USER_DETAIL = "auth:user:detail:";
}
