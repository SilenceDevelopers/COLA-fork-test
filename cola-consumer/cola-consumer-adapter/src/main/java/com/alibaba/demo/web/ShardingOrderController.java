package com.alibaba.demo.web;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.ShardingOrderService;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("shardingOrder")
public class ShardingOrderController {

    @Autowired
    private ShardingOrderService shardingOrderService;

    @Autowired
    private Snowflake snowflake;


    @PostMapping("/add")
    public Response add(@RequestBody ShardingOrderAddCmd shardingOrderAddCmd) {
        return shardingOrderService.add(shardingOrderAddCmd);
    }

    @PostMapping("/getList")
    public List<ShardingOrderListVO> getList(@RequestBody ShardingOrderListQueryCmd cmd) {
        return shardingOrderService.getList(cmd);
    }

    @PostMapping("/getSnowflakeIds")
    public void getSnowflakeIds() {
        HashMap<Long, Integer> map = Maps.newHashMap();
        int count = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < 1000; j++) {
                map.put(snowflake.nextId(), 1);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("结束时间：" + end + ",开始时间：" + start + ",生产雪花id总耗时：" + (end - start));
        System.out.println("雪花id生产总数量：" + map.size());
    }

}
