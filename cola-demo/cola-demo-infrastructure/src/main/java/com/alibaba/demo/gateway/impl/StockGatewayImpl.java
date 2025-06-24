package com.alibaba.demo.gateway.impl;

import com.alibaba.demo.domain.customer.gateway.StockGateway;
import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockGatewayImpl implements StockGateway {

    @Autowired
    private StockMapper stockMapper;

    @Override
    public Stock getDetail(StockDetailQueryCmd cmd) {
        return stockMapper.getDetail(cmd);
    }

    @Override
    public Integer reduceStock(Stock stock) {
        return stockMapper.reduceStock(stock);
    }
}
