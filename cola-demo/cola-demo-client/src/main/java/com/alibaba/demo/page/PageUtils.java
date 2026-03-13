package com.alibaba.demo.page;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {
    /**
     * 将 MyBatis-Plus 的 IPage 转换为自定义的 PageResult (最常用)
     * @param iPage MyBatis-Plus 分页结果
     * @param <T> 记录类型
     * @return 自定义分页结果
     */
    public static <T> PageResult<T> toPageResult(IPage<T> iPage) {
        if (iPage == null) {
            return emptyPageResult();
        }
        return new PageResult<>(
                iPage.getRecords(),
                iPage.getTotal(),
                iPage.getCurrent(),
                iPage.getSize()
        );
    }

    /**
     * 将 IPage 转换并提取其中的记录为另一种类型 (常用于 DO 转 VO)
     * @param iPage  MyBatis-Plus 分页结果
     * @param mapper 记录类型转换函数
     * @param <T>    源类型
     * @param <R>    目标类型
     * @return 转换后的自定义分页结果
     */
    public static <T, R> PageResult<R> toPageResult(IPage<T> iPage, Function<T, R> mapper) {
        if (iPage == null) {
            return emptyPageResult();
        }
        List<R> convertedList = iPage.getRecords()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageResult<>(
                convertedList,
                iPage.getTotal(),
                iPage.getCurrent(),
                iPage.getSize()
        );
    }

    /**
     * 在内存中进行分页 (适用于已获取全部List的场景)
     * @param sourceList 源数据列表
     * @param pageParam  分页参数
     * @param <T>        记录类型
     * @return 自定义分页结果
     */
    public static <T> PageResult<T> paginateInMemory(List<T> sourceList, PageParam pageParam) {
        if (sourceList == null || sourceList.isEmpty()) {
            return emptyPageResult();
        }
        long total = sourceList.size();
        long current = pageParam.getCurrent();
        long size = pageParam.getSize();
        long start = (current - 1) * size;
        if (start >= total) {
            return new PageResult<>(Collections.emptyList(), total, current, size);
        }
        long end = Math.min(start + size, total);
        List<T> pageList = sourceList.subList((int) start, (int) end);
        return new PageResult<>(pageList, total, current, size);
    }

    /**
     * 创建一个空的分页结果
     */
    public static <T> PageResult<T> emptyPageResult() {
        return new PageResult<>();
    }

    /**
     * 判断 IPage 是否有数据
     */
    public static <T> boolean hasData(IPage<T> iPage) {
        return iPage != null && iPage.getRecords() != null && !iPage.getRecords().isEmpty();
    }
}
