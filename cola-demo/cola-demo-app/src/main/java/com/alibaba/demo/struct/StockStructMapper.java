package com.alibaba.demo.struct;

import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dto.vo.StockVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockStructMapper {

    StockVO toVO(Stock stock);
}
