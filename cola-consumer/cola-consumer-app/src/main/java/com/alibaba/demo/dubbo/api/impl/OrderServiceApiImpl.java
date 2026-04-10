package com.alibaba.demo.dubbo.api.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dubbo.api.OrderServiceApi;
import com.alibaba.demo.dubbo.dto.AddOrderDTO;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;
import com.alibaba.demo.struct.ShardingOrderStructMapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DubboService(filter = "seataFilter")
@RequiredArgsConstructor
public class OrderServiceApiImpl implements OrderServiceApi {

    private final ShardingOrderGateway shardingOrderGateway;

    private final Snowflake snowflake;

    private final ShardingOrderStructMapper shardingOrderStructMapper;

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

    @Override
    public List<ShardingOrderDTO> getOrderList() {
        List<ShardingOrder> orders = shardingOrderGateway.getList(null);
        List<ShardingOrderDTO> list = orders.stream().map(shardingOrderStructMapper::toVO).toList();
        return list;
    }
}
