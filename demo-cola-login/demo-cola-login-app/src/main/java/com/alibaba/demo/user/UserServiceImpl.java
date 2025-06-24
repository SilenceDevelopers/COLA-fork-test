package com.alibaba.demo.user;

import com.alibaba.demo.api.UserService;
import com.alibaba.demo.constants.AuthConstant;
import com.alibaba.demo.dto.UserLoginDTO;
import com.alibaba.demo.result.BaseResult;
import com.alibaba.demo.util.JwtUtil;
import com.alibaba.demo.util.RedisUtil;
import com.alibaba.demo.util.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.alibaba.demo.constants.AuthConstant.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public BaseResult<String> login(UserLoginDTO userLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication == null) {
            return BaseResult.fail("用户名或密码错误");
        }
        // 如果验证成功, 就生成Token并返回
        MySysUserDetails userDetails = (MySysUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        Map<String,Object> map = Maps.newHashMap();
        map.put("userId",userId);
        String token = jwtUtil.generateToken(map,"");
        ServletUtil.getResponse().setHeader(AuthConstant.TOKEN_HEADER, TOKEN_PREFIX + token);
        // 将token存入Redis中
        redisUtil.set(REDIS_KEY_AUTH_TOKEN + userId, token, 86400L);
        // 将UserDetails存入redis中
        redisUtil.set(REDIS_KEY_AUTH_USER_DETAIL + userId, JSON.toJSONString(userDetails), 86400L);
        String userJson = JSON.toJSONString(authentication.getDetails());
        return BaseResult.success(userJson);
    }
}
