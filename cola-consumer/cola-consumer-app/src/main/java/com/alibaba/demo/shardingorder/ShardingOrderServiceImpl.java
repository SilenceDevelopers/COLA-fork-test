package com.alibaba.demo.shardingorder;

import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.ShardingOrderService;
import com.alibaba.demo.dto.data.ShardingOrderAddCmd;
import com.alibaba.demo.dto.data.ShardingOrderListQueryCmd;
import com.alibaba.demo.dto.vo.ShardingOrderListVO;
import com.alibaba.demo.shardingorder.executor.ShardingOrderAddCmdExe;
import com.alibaba.demo.shardingorder.executor.query.ShardingOrderQueryCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShardingOrderServiceImpl implements ShardingOrderService {

    @Autowired
    private ShardingOrderAddCmdExe shardingOrderAddCmdExe;

    @Autowired
    private ShardingOrderQueryCmdExe shardingOrderQueryCmdExe;

    @Override
    public Response add(ShardingOrderAddCmd shardingOrderAddCmd) {
        return shardingOrderAddCmdExe.execute(shardingOrderAddCmd);
    }

    @Override
    public List<ShardingOrderListVO> getList(ShardingOrderListQueryCmd shardingOrderListQueryCmd) {
        return shardingOrderQueryCmdExe.execute(shardingOrderListQueryCmd);
    }
}
