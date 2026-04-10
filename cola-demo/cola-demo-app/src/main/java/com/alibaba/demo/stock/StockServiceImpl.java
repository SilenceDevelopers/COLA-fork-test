package com.alibaba.demo.stock;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.StockService;
import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.alibaba.demo.dto.StockReduceCmd;
import com.alibaba.demo.dto.vo.StockVO;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;
import com.alibaba.demo.order.OrderServiceExe;
import com.alibaba.demo.page.PageResult;
import com.alibaba.demo.page.PageUtils;
import com.alibaba.demo.response.ApiResponse;
import com.alibaba.demo.stock.executor.StockReduceExe;
import com.alibaba.demo.stock.executor.query.StockDetailQueryExe;
import com.alibaba.demo.struct.StockStructMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockDetailQueryExe stockDetailQueryExe;
    private final StockReduceExe stockReduceExe;
    private final StockStructMapper stockStructMapper;
    private final OrderServiceExe orderServiceExe;

    @Override
    public String getDetail(StockDetailQueryCmd cmd) {
        return stockDetailQueryExe.getDetail(cmd);
    }

    @Override
    public Response reduceStock(StockReduceCmd cmd) {
        return stockReduceExe.reduceStock(cmd);
    }

    @Override
    public PageResult<StockVO> getStockPage(StockQuery query) {
        // 1. 从 queryParam 中获取分页参数，创建 MyBatis-Plus 的 Page 对象
        Page<Stock> page = new Page<>(query.getCurrent(), query.getSize());

        // 2. 执行分页查询，传入 page 和 queryParam
        IPage<Stock> userPage = stockDetailQueryExe.getStockPage(page, query);

        // 3. 转换为自定义的 PageResult（使用之前的 PageUtils）
        return PageUtils.toPageResult(userPage, stockStructMapper::toVO);
    }

    @Override
    public List<ShardingOrderDTO> getOrder() {
        return orderServiceExe.getOrderList();
    }
}
