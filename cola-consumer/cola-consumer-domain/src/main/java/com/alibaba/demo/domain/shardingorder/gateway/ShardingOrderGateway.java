package com.alibaba.demo.domain.shardingorder.gateway;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import com.alibaba.demo.dto.data.ShardingOrderESQueryCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.page.PageInfo;

import java.util.List;

public interface ShardingOrderGateway {

    void add(ShardingOrder shardingOrder);

    List<ShardingOrder> getList(ShardingOrderListQueryCmd cmd);

    PageInfo<ShardingOrderDocument> getPage(ShardingOrderESQueryCmd cmd);
}
