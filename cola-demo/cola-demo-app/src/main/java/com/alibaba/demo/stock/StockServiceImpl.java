package com.alibaba.demo.stock;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.StockService;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.stock.executor.StockReduceExe;
import com.alibaba.demo.stock.executor.query.StockDetailQueryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDetailQueryExe stockDetailQueryExe;

    @Autowired
    private StockReduceExe stockReduceExe;

    @Override
    public String getDetail(StockDetailQueryCmd cmd) {
        return stockDetailQueryExe.getDetail(cmd);
    }

    @Override
    public Response reduceStock(StockReduceCmd cmd) {
        return stockReduceExe.reduceStock(cmd);
    }
}
