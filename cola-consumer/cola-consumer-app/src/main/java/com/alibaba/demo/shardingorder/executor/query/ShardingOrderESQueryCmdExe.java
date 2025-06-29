package com.alibaba.demo.shardingorder.executor.query;

import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderESQueryCmd;
import com.alibaba.demo.dto.page.PageInfo;
import com.alibaba.demo.dto.vo.ShardingOrderESPageVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ShardingOrderESQueryCmdExe {

    @Autowired
    private ShardingOrderGateway shardingOrderGateway;

    public PageInfo<ShardingOrderESPageVO> execute(ShardingOrderESQueryCmd cmd) {
        PageInfo<ShardingOrderDocument> pageInfo = shardingOrderGateway.getPage(cmd);
        List<ShardingOrderESPageVO> list = Optional.ofNullable(pageInfo.getData()).orElseGet(Lists::newArrayList).stream().map(l-> new ShardingOrderESPageVO(l.getId(),l.getOrderName(),l.getOrderDesc(),l.getOrderPrice(),l.getStoreId())).toList();
        PageInfo<ShardingOrderESPageVO> result = new PageInfo<>(list);
        result.setPages(pageInfo.getPages());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPageNum(pageInfo.getPageNum());
        return result;
    }
}
