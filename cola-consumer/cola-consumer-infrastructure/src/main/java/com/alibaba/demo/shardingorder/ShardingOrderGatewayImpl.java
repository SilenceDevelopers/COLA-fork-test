package com.alibaba.demo.shardingorder;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.mapper.ShardingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShardingOrderGatewayImpl implements ShardingOrderGateway {

    @Autowired
    private ShardingOrderMapper shardingOrderMapper;

    @Autowired
    private Snowflake snowflake;

    @Override
    public void add(ShardingOrder shardingOrder) {
        shardingOrder.setId(snowflake.nextId());
        shardingOrderMapper.insertShardingOrder(shardingOrder);
    }

    @Override
    public List<ShardingOrder> getList(ShardingOrderListQueryCmd cmd) {
        return shardingOrderMapper.getList(cmd);
    }
}
