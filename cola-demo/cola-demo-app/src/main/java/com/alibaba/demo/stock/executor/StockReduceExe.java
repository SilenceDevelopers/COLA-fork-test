package com.alibaba.demo.stock.executor;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.domain.customer.gateway.StockGateway;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.dubbo.api.OrderServiceApi;
import com.alibaba.demo.dubbo.dto.AddOrderDTO;
import com.alibaba.demo.stock.executor.assembler.StockAssembler;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockReduceExe {

    @Autowired
    private StockGateway stockGateway;

    @DubboReference
    private OrderServiceApi orderServiceApi;

    @GlobalTransactional
    public Response reduceStock(StockReduceCmd cmd) {
        int i = stockGateway.reduceStock(StockAssembler.toReduce(cmd));
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setStoreId(cmd.getId());
        addOrderDTO.setName("测试");
        addOrderDTO.setPrice(cmd.getPrice());
        addOrderDTO.setNum(cmd.getReduceNum());
        RpcContext.getServerContext().setAttachment(RootContext.KEY_XID, RootContext.getXID());
        log.info("provider seata XID:" + RootContext.getXID());
        orderServiceApi.createOrder(addOrderDTO);
        return Response.buildSuccess();
    }
}
