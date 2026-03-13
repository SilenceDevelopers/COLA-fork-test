package com.alibaba.demo.page;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageResult<T> {
    // 核心数据
    private List<T> list = Collections.emptyList();
    // 核心分页信息
    private Long total = 0L;
    private Long current = 1L;
    private Long size = 10L;
    private Long pages = 0L;

    // 成功响应的快捷构造方法
    public PageResult(List<T> list, Long total, Long current, Long size) {
        this.list = list;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size; // 计算总页数
    }

    public PageResult() {
    }
}
