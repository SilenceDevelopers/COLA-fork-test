package com.alibaba.demo.es;

import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductESRepository extends ElasticsearchRepository<ShardingOrderDocument, Long> {
}
