package com.alibaba.demo.mapper;

import com.alibaba.demo.customer.CustomerDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<CustomerDO> {

    CustomerDO getById(String customerId);
}
