package com.alibaba.demo.gateway.impl;

import com.alibaba.demo.domain.customer.gateway.StockGateway;
import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.alibaba.demo.mapper.StockMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage<Stock> getStockPage(Page<Stock> page, StockQuery query) {
        return stockMapper.getStockPage(page, query);
    }
}
