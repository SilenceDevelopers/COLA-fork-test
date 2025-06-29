package com.alibaba.demo.domain.shardingorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;

@Data
@Document(indexName = "order_index")
@NoArgsConstructor
@AllArgsConstructor
public class ShardingOrderDocument {
    @Id
    private Long id;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart"), otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String orderName;
    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart"), otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String orderDesc;
    @Field(type = FieldType.Double)
    private BigDecimal orderPrice;
    @Field(type = FieldType.Long)
    private Long storeId;
}
