package com.alibaba.demo.stock.executor.assembler;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.dto.vo.StockVO;

public class StockAssembler {

    public static StockVO toVO(Stock stock) {
        if (stock == null) {
            return null;
        }
        StockVO vo = new StockVO();
        vo.setId(stock.getId());
        vo.setName(stock.getName());
        vo.setNum(stock.getNum());
        vo.setVersion(stock.getVersion());
        return vo;
    }

    public static Stock toReduce(StockReduceCmd cmd) {
        if (cmd == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(cmd.getId());
        stock.setNum(cmd.getReduceNum());
        return stock;
    }
}
