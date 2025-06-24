package com.alibaba.demo.dto.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShardingOrderAddCmd {

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;
}
