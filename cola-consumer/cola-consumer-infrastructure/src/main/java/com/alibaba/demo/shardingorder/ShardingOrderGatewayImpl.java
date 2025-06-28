package com.alibaba.demo.shardingorder;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.mapper.ShardingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShardingOrderGatewayImpl implements ShardingOrderGateway {

    @Autowired
    private ShardingOrderMapper shardingOrderMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public void add(ShardingOrder shardingOrder) {
        shardingOrder.setId(snowflake.nextId());
        shardingOrderMapper.insertShardingOrder(shardingOrder);
        ShardingOrderDocument document = new ShardingOrderDocument(shardingOrder.getId(), shardingOrder.getOrderName(), shardingOrder.getOrderDesc(), shardingOrder.getOrderPrice(), shardingOrder.getStoreId());
        IndexQuery indexQuery = new IndexQueryBuilder().withId(shardingOrder.getId().toString()).withObject(document).build();
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of("order_index"));
    }

    @Override
    public List<ShardingOrder> getList(ShardingOrderListQueryCmd cmd) {
        return shardingOrderMapper.getList(cmd);
    }
}
