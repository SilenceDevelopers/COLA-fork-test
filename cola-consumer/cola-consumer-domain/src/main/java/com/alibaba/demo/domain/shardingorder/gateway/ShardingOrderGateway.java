package com.alibaba.demo.domain.shardingorder.gateway;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;

import java.util.List;

public interface ShardingOrderGateway {

    void add(ShardingOrder shardingOrder);

    List<ShardingOrder> getList(ShardingOrderListQueryCmd cmd);
}
