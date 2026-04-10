package com.alibaba.demo.domain.customer.gateway;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface StockGateway {

    Stock getDetail(StockDetailQueryCmd cmd);

    Integer reduceStock(Stock stock);

    IPage<Stock> getStockPage(Page<Stock> page, StockQuery query);

    Stock getDetailById(Long id);
}
