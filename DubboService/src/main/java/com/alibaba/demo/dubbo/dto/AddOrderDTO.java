package com.alibaba.demo.dubbo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AddOrderDTO implements Serializable {

    private Long storeId;

    private String name;

    private Integer num;

    private BigDecimal price;
}
