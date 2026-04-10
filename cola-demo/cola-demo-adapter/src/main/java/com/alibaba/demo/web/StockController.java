package com.alibaba.demo.web;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.CustomerServiceI;
import com.alibaba.demo.api.StockService;
import com.alibaba.demo.dto.CustomerListByNameQry;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;
import com.alibaba.demo.response.ApiResponse;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    private final CustomerServiceI customerServiceI;

    @PostMapping("/getDetailById")
    public String getDetailById(@RequestBody StockDetailQueryCmd cmd) {
        return stockService.getDetail(cmd);
    }

    @PostMapping("/test")
    public String test(@RequestBody CustomerListByNameQry qry) {
        return JSON.toJSONString(customerServiceI.listByName(qry));
    }

    /**
     * 扣减库存-下订单
     *
     * @param cmd
     * @return
     */
    @PostMapping("/reduceStock")
    public Response reduceStock(@RequestBody StockReduceCmd cmd) {
        return stockService.reduceStock(cmd);
    }

    @PostMapping("/getPage")
    public ApiResponse getPage(@RequestBody StockQuery stockQuery) {
        return ApiResponse.success(stockService.getStockPage(stockQuery));
    }

    @PostMapping("/getOrder")
    public List<ShardingOrderDTO> getOrder(){
        return stockService.getOrder();
    }
}
