package com.alibaba.demo.mapper;

import com.alibaba.demo.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper{

  CustomerDO getById(String customerId);
}
