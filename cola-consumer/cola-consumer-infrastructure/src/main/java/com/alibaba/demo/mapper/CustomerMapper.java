package com.alibaba.demo.mapper;

import com.alibaba.demo.customer.CustomerDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends BaseMapper<CustomerDO> {

    CustomerDO getById(String customerId);
}
