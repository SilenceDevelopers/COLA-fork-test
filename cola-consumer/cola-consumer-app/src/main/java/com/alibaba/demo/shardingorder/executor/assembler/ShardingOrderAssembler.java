package com.alibaba.demo.shardingorder.executor.assembler;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ShardingOrderAssembler {

    public static ShardingOrder toSave(ShardingOrderAddCmd shardingOrderAddCmd) {
        ShardingOrder order = new ShardingOrder();
        order.setOrderName(shardingOrderAddCmd.getOrderName());
        order.setOrderDesc(shardingOrderAddCmd.getOrderDesc());
        order.setOrderPrice(shardingOrderAddCmd.getOrderPrice());
        order.setStoreId(shardingOrderAddCmd.getStoreId());
        return order;
    }

    public static List<ShardingOrderListVO> toListVO(List<ShardingOrder> shardingOrders) {
        if (CollectionUtils.isEmpty(shardingOrders)) {
            return Lists.newArrayList();
        }
        return shardingOrders.parallelStream().map(c -> {
            ShardingOrderListVO vo = new ShardingOrderListVO();
            vo.setId(c.getId());
            vo.setOrderName(c.getOrderName());
            vo.setOrderDesc(c.getOrderDesc());
            vo.setOrderPrice(c.getOrderPrice());
            vo.setStoreId(c.getStoreId());
            return vo;
        }).collect(Collectors.toList());
    }
}
