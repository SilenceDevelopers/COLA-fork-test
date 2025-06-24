//package com.alibaba.demo.filter;
//
//import com.alibaba.demo.constants.AuthConstant;
//import com.alibaba.demo.dto.UserLoginDTO;
//import com.alibaba.demo.result.BaseResult;
//import com.alibaba.demo.result.ResultEnum;
//import com.alibaba.demo.user.MySysUserDetails;
//import com.alibaba.demo.util.JwtUtil;
//import com.alibaba.demo.util.RedisUtil;
//import com.alibaba.demo.util.ServletUtil;
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Maps;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//import static com.alibaba.demo.constants.AuthConstant.*;
//
//@Component
//@Slf4j
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private RedisUtil redisUtil;
//
//    private long expire;
//
//    public LoginFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil, long expire) {
//        super(authenticationManager);
//        this.redisUtil = redisUtil;
//        this.expire = expire;
//        super.setPostOnly(true);
//        super.setFilterProcessesUrl("/sys/login");
//        super.setUsernameParameter("username");
//        super.setPasswordParameter("password");
//    }
//
//    @SneakyThrows
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        log.info("LoginFilter authentication start");
//        // 数据是通过 RequestBody 传输
//        UserLoginDTO userLoginDTO = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8, UserLoginDTO.class);
//
//        return super.getAuthenticationManager().authenticate(
//                new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) {
//        log.info("LoginFilter authentication success: {}", authResult);
//        // 如果验证成功, 就生成Token并返回
//        MySysUserDetails userDetails = (MySysUserDetails) authResult.getPrincipal();
//        Long userId = userDetails.getId();
//        Map<String,Object> map = Maps.newHashMap();
//        map.put("userId",userId);
//        String token = JwtUtil.generateToken(map,"");
//        response.setHeader(AuthConstant.TOKEN_HEADER, TOKEN_PREFIX + token);
//        // 将token存入Redis中
//        redisUtil.set(REDIS_KEY_AUTH_TOKEN + userId, token, expire);
//        log.info("YaLoginFilter authentication end");
//        // 将UserDetails存入redis中
//        redisUtil.set(REDIS_KEY_AUTH_USER_DETAIL + userId, JSON.toJSONString(userDetails), 86400L);
//
//        ServletUtil.renderResult(response, new BaseResult<>(ResultEnum.SUCCESS.code, "登陆成功"));
//        log.info("LoginFilter authentication end");
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                              AuthenticationException failed) throws IOException {
//        log.info("LoginFilter authentication failed: {}", failed.getMessage());
//        ServletUtil.renderResult(response, new BaseResult<>(ResultEnum.FAILED_UNAUTHORIZED.code, "登陆失败：" + failed.getMessage()));
//    }
//}
