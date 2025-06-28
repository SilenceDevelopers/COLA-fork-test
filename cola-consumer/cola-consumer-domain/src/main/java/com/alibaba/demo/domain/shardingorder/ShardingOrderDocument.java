package com.alibaba.demo.domain.shardingorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Data
@Document(indexName = "order_index")
@NoArgsConstructor
@AllArgsConstructor
public class ShardingOrderDocument {
    @Id
    private Long id;

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;

    private Long storeId;
}
