package com.alibaba.demo.mapper;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.StockDetailQueryCmd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    Stock getDetail(@Param("cmd") StockDetailQueryCmd cmd);

    @Update("update stock set num = num - #{cmd.num} where id = #{cmd.id}")
    int reduceStock(@Param("cmd") Stock stock);
}
