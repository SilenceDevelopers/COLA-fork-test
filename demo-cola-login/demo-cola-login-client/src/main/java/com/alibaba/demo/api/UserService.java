package com.alibaba.demo.api;

import com.alibaba.demo.dto.UserLoginDTO;
import com.alibaba.demo.result.BaseResult;

public interface UserService {

    BaseResult<String> login(UserLoginDTO userLoginDTO);
}
