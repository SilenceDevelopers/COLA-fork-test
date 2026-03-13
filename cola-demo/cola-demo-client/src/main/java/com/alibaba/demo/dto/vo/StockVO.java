package com.alibaba.demo.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockVO {

    private Long id;

    private String name;

    private Integer num;

    private Integer version;
}
