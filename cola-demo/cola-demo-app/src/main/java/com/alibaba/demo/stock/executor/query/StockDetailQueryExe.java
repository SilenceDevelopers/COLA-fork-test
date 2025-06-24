package com.alibaba.demo.stock.executor.query;

import com.alibaba.demo.domain.customer.gateway.StockGateway;
import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.stock.executor.assembler.StockAssembler;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockDetailQueryExe {

    @Autowired
    private StockGateway stockGateway;

    public String getDetail(StockDetailQueryCmd cmd) {
        Stock stock = stockGateway.getDetail(cmd);
        return JSONObject.toJSONString(StockAssembler.toVO(stock));
    }
}
