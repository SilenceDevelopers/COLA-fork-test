package com.alibaba.demo.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.data.ShardingOrderESQueryCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.page.PageInfo;
import com.alibaba.demo.dto.vo.ShardingOrderESPageVO;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.alibaba.demo.result.BaseResult;

import java.util.List;

public interface ShardingOrderService {
    Response add(ShardingOrderAddCmd shardingOrderAddCmd);

    List<ShardingOrderListVO> getList(ShardingOrderListQueryCmd shardingOrderListQueryCmd);

    BaseResult<PageInfo<ShardingOrderESPageVO>> getPage(ShardingOrderESQueryCmd cmd);
}
