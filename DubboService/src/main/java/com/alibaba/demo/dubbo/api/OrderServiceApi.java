package com.alibaba.demo.dubbo.api;

import com.alibaba.demo.dubbo.dto.AddOrderDTO;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;

import java.util.List;

public interface OrderServiceApi {

    int createOrder(AddOrderDTO addOrderDTO);

    List<ShardingOrderDTO> getOrderList();
}
