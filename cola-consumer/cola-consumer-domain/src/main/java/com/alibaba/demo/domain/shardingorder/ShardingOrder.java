package com.alibaba.demo.domain.shardingorder;

import com.alibaba.cola.domain.Entity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class ShardingOrder {

    @TableId
    private Long id;

    private String orderName;

    private String orderDesc;

    private BigDecimal orderPrice;

    private Long storeId;
}
