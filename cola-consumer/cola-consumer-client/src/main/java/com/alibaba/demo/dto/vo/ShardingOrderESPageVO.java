package com.alibaba.demo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShardingOrderESPageVO {
    private Long id;

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;

    private Long storeId;
}
