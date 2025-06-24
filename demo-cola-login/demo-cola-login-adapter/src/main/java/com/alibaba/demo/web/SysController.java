package com.alibaba.demo.web;

import com.alibaba.demo.api.UserService;
import com.alibaba.demo.dto.UserLoginDTO;
import com.alibaba.demo.result.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public BaseResult<String> login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/index")
    public String index() {
        return "用户登录成功";
    }
}
