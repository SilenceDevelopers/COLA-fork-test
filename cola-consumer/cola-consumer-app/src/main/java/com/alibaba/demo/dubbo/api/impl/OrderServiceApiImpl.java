package com.alibaba.demo.dubbo.api.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dubbo.api.OrderServiceApi;
import com.alibaba.demo.dubbo.dto.AddOrderDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(filter = "seataFilter")
public class OrderServiceApiImpl implements OrderServiceApi {

    @Autowired
    private ShardingOrderGateway shardingOrderGateway;

    @Autowired
    private Snowflake snowflake;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOrder(AddOrderDTO addOrderDTO) {
        ShardingOrder shardingOrder = new ShardingOrder();
        shardingOrder.setId(snowflake.nextId());
        shardingOrder.setOrderDesc("123");
        shardingOrder.setOrderName(addOrderDTO.getName());
        shardingOrder.setOrderPrice(addOrderDTO.getPrice());
        shardingOrder.setStoreId(addOrderDTO.getStoreId());
        shardingOrderGateway.add(shardingOrder);
        return 0;
    }
}
