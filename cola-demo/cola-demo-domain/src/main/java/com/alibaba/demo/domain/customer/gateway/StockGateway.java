package com.alibaba.demo.domain.customer.gateway;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;

public interface StockGateway {

    Stock getDetail(StockDetailQueryCmd cmd);

    Integer reduceStock(Stock stock);
}
