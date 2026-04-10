package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.alibaba.demo.dto.StockQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    Stock getDetail(@Param("cmd") StockDetailQueryCmd cmd);

    @Update("update stock set num = num - #{cmd.num} where id = #{cmd.id}")
    int reduceStock(@Param("cmd") Stock stock);

    @Select("select * from stock order by id desc")
    IPage<Stock> getStockPage(Page<?> page, @Param("query") StockQuery query);

    @Select("select * from stock where id = #{id}")
    Stock getDetailById(@Param("id") Long id);
}
