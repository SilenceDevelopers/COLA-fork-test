package com.alibaba.demo.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.dto.vo.StockVO;
import com.alibaba.demo.page.PageResult;

public interface StockService {

    String getDetail(StockDetailQueryCmd cmd);

    Response reduceStock(StockReduceCmd cmd);

    PageResult<StockVO> getStockPage(StockQuery query);
}
