package com.alibaba.demo.filter;

import com.alibaba.demo.user.MySysUserDetails;
import com.alibaba.demo.util.JwtUtil;
import com.alibaba.demo.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.alibaba.demo.constants.AuthConstant.REDIS_KEY_AUTH_USER_DETAIL;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//获取请求头中的token
        String token = request.getHeader("token");
        System.out.println("前端的token信息=======>" + token);
        //如果token为空直接放行，由于用户信息没有存放在SecurityContextHolder.getContext()中所以后面的过滤器依旧认证失败符合要求
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

//        解析Jwt中的用户id
        Long userId = (Long) jwtUtil.parseToken(token).get("userId");

        //从redis中获取用户信息
        String redisUser = (String) redisUtil.get(REDIS_KEY_AUTH_USER_DETAIL + userId);
        if (!StringUtils.hasText(redisUser)) {
            filterChain.doFilter(request, response);
            return;
        }

        MySysUserDetails userDetails = JSON.parseObject(redisUser, MySysUserDetails.class);

        //将用户信息存放在SecurityContextHolder.getContext()，后面的过滤器就可以获得用户信息了。这表明当前这个用户是登录过的，后续的拦截器就不用再拦截了
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
