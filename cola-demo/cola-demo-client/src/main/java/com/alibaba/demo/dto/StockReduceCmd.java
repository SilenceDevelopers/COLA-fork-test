package com.alibaba.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockReduceCmd {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "扣减数量不能为空")
    private Integer reduceNum;

    @NotNull(message = "购买金额不能为空")
    private BigDecimal price;
}
