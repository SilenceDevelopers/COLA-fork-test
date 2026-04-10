package com.alibaba.demo.order;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.dubbo.api.OrderServiceApi;
import com.alibaba.demo.dubbo.dto.ShardingOrderDTO;
import com.alibaba.demo.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderServiceExe {

    @DubboReference(protocol = "dubbo")
    private OrderServiceApi orderServiceApi;

    public List<ShardingOrderDTO> getOrderList(){
        List<ShardingOrderDTO> list = orderServiceApi.getOrderList();
        return list;
    }
}
