package com.alibaba.demo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShardingOrderListVO {

    private Long id;

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;

    private Long storeId;

}
