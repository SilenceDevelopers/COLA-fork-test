package com.alibaba.demo.dubbo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ShardingOrderDTO implements Serializable {
    private Long id;

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;

    private Long storeId;
}
