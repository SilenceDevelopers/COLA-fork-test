package com.alibaba.demo.dubbo.api;

import com.alibaba.demo.dubbo.dto.AddOrderDTO;

public interface OrderServiceApi {

    int createOrder(AddOrderDTO addOrderDTO);
}
