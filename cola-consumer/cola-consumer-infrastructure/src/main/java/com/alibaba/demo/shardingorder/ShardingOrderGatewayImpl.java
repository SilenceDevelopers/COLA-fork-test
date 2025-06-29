package com.alibaba.demo.shardingorder;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.demo.domain.shardingorder.ShardingOrder;
import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import com.alibaba.demo.domain.shardingorder.gateway.ShardingOrderGateway;
import com.alibaba.demo.dto.data.ShardingOrderESQueryCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.page.PageInfo;
import com.alibaba.demo.mapper.ShardingOrderMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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

    @Override
    public PageInfo<ShardingOrderDocument> getPage(ShardingOrderESQueryCmd cmd) {
        Page<ShardingOrderDocument> page = PageHelper.startPage(cmd.getPageNo(), cmd.getPageSize());
        Criteria criteria = new Criteria("orderName").contains(cmd.getKeyword()).or(new Criteria("orderDesc").contains(cmd.getKeyword()));
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria, PageRequest.of(cmd.getPageNo(), cmd.getPageSize()));
        SearchHits<ShardingOrderDocument> hits = elasticsearchOperations.search(criteriaQuery, ShardingOrderDocument.class);
        List<ShardingOrderDocument> list = hits.getSearchHits().stream().map(SearchHit::getContent).toList();
        PageInfo<ShardingOrderDocument> pageInfo = new PageInfo<>(page);
        pageInfo.setData(list);
        return pageInfo;
    }
}
