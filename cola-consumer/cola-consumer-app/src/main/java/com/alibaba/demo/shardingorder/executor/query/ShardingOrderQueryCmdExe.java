package com.alibaba.demo.shardingorder.executor.query;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.alibaba.demo.shardingorder.executor.assembler.ShardingOrderAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShardingOrderQueryCmdExe {

    @Autowired
    private ShardingOrderGateway shardingOrderGateway;

    public List<ShardingOrderListVO> execute(ShardingOrderListQueryCmd cmd) {
        List<ShardingOrder> shardingOrders = shardingOrderGateway.getList(cmd);
        return ShardingOrderAssembler.toListVO(shardingOrders);
    }
}
