package com.alibaba.demo.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageParam {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 1000;

    @Min(value = 1, message = "页码不能小于1")
    private Integer current = DEFAULT_PAGE;

    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = MAX_PAGE_SIZE, message = "每页条数不能超过 {value}")
    private Integer size = DEFAULT_PAGE_SIZE;

    // 快速创建MyBatis-Plus Page对象的方法
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toMpPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
    }
}
