package com.alibaba.demo.struct;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShardingOrderStructMapper {
    ShardingOrderDTO toVO(ShardingOrder shardingOrder);
}
