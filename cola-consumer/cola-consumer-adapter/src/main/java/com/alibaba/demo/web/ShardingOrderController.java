package com.alibaba.demo.web;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.ShardingOrderService;
import com.alibaba.demo.config.SafeGeneticSnowflakeIdGenerator;
import com.alibaba.demo.config.ShardingRouter;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.data.ShardingOrderESQueryCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.page.PageInfo;
import com.alibaba.demo.dto.vo.ShardingOrderESPageVO;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.alibaba.demo.result.BaseResult;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("shardingOrder")
@Slf4j
public class ShardingOrderController {

    @Autowired
    private ShardingOrderService shardingOrderService;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private SafeGeneticSnowflakeIdGenerator idGenerator;

    @Autowired
    private ShardingRouter shardingRouter;


    @PostMapping("/add")
    public Response add(@RequestBody ShardingOrderAddCmd shardingOrderAddCmd) {
        return shardingOrderService.add(shardingOrderAddCmd);
    }

    @PostMapping("/getList")
    public List<ShardingOrderListVO> getList(@RequestBody ShardingOrderListQueryCmd cmd) {
        return shardingOrderService.getList(cmd);
    }

    @PostMapping("/getPage")
    public BaseResult<PageInfo<ShardingOrderESPageVO>> getPage(@RequestBody ShardingOrderESQueryCmd cmd) {
        return shardingOrderService.getPage(cmd);
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

    @PostMapping("/createOrder")
    public Long createOrder() {
        Long userId = snowflake.nextId();
        Long orderId = idGenerator.nextId(userId);

        int[] shardInfo = shardingRouter.calculateShard(SafeGeneticSnowflakeIdGenerator.hashGene(userId));
        String physicalTable = shardingRouter.getPhysicalTableName(shardInfo, "order");
        log.info("Order {} created and routed to {}", orderId, physicalTable);
        return orderId;
    }

}
