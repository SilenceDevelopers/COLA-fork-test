package com.alibaba.demo.es;

import com.alibaba.demo.domain.shardingorder.ShardingOrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<ShardingOrderDocument, Long> {
}
