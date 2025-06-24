package com.alibaba.demo.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;

import java.util.List;

public interface ShardingOrderService {
    Response add(ShardingOrderAddCmd shardingOrderAddCmd);

    List<ShardingOrderListVO> getList(ShardingOrderListQueryCmd shardingOrderListQueryCmd);
}
