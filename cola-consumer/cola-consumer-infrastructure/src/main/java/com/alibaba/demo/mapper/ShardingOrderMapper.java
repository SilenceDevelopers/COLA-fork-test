package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ShardingOrderMapper extends BaseMapper<ShardingOrder> {

    @Insert("insert into sharding_order(id, order_name, order_desc, order_price, store_id) values(#{id}, #{orderName}, #{orderDesc}, #{orderPrice}, #{storeId})")
    void insertShardingOrder(ShardingOrder shardingOrder);

    @Select("select * from sharding_order")
    List<ShardingOrder> getList(ShardingOrderListQueryCmd cmd);
}
