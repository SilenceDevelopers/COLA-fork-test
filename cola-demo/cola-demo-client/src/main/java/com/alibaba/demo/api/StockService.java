package com.alibaba.demo.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockReduceCmd;

public interface StockService {

    String getDetail(StockDetailQueryCmd cmd);

    Response reduceStock(StockReduceCmd cmd);
}
