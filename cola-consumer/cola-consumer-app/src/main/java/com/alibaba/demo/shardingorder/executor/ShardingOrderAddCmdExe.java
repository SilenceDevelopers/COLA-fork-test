package com.alibaba.demo.shardingorder.executor;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.shardingorder.executor.assembler.ShardingOrderAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShardingOrderAddCmdExe {

    @Autowired
    private ShardingOrderGateway shardingOrderGateway;

    public Response execute(ShardingOrderAddCmd shardingOrderAddCmd) {
        shardingOrderGateway.add(ShardingOrderAssembler.toSave(shardingOrderAddCmd));
        return Response.buildSuccess();
    }
}
