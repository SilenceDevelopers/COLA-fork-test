package com.alibaba.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.seata.core.constants.DubboConstants;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.tm.api.GlobalTransactionContext;
import org.apache.shardingsphere.transaction.base.seata.at.SeataTransactionHolder;

@Slf4j
@Activate(group = {DubboConstants.CONSUMER})
public class SeataFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String xid = RpcContext.getServerAttachment().getAttachment(RootContext.KEY_XID);
        log.info("Seata xid:" + xid);
        if (!StringUtils.isBlank(xid) && SeataTransactionHolder.get() == null) {
            RootContext.bind(xid);
            SeataTransactionHolder.set(GlobalTransactionContext.getCurrentOrCreate());
        }
        Result result = invoker.invoke(invocation);
        return result;
    }
}
