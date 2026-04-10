package com.alibaba.demo.domain.stock;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stock")
public class Stock {

    @TableId
    private Long id;

    private String name;

    private Integer num;

    private Integer version;

    private String des;
}
