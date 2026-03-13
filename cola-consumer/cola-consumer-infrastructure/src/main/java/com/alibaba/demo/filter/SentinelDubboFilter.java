//package com.alibaba.demo.filter;
//
//import com.alibaba.csp.sentinel.Entry;
//import com.alibaba.csp.sentinel.EntryType;
//import com.alibaba.csp.sentinel.SphU;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import org.apache.dubbo.common.constants.CommonConstants;
//import org.apache.dubbo.common.extension.Activate;
//import org.apache.dubbo.rpc.*;
//
//import java.util.concurrent.TimeoutException;
//
//@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER})
//public class SentinelDubboFilter implements Filter {
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        boolean isProviderSide = RpcContext.getCurrentServiceContext().isProviderSide();
//        EntryType entryType = isProviderSide ? EntryType.IN : EntryType.OUT;
//        String resourceName = getResourceName(invoker, invocation);
//        Entry entry = null;
//        try {
//            entry = SphU.entry(resourceName, entryType,1,invocation.getArguments());
//            return invoker.invoke(invocation);
//        } catch (BlockException e) {
//            // 统一限流处理
//            return handleBlockException(invocation, e);
//        } catch (Throwable e) {
//            // 统一降级处理
//            if (shouldDegrade(e)) {
//                return handleFallback(invocation, e);
//            }
//            throw new RpcException(e);
//        } finally {
//            if (entry != null) {
//                entry.exit();
//            }
//        }
//    }
//
//    private String getResourceName(Invoker<?> invoker, Invocation invocation) {
//        // 生成资源名称: 接口名:方法名
//        return invoker.getInterface().getSimpleName() + ":" + invocation.getMethodName();
//    }
//
//    private Result handleBlockException(Invocation invocation, BlockException e) {
//        // 返回限流结果
//        return new AppResponse("服务繁忙，请稍后重试");
//    }
//
//    private Result handleFallback(Invocation invocation, Throwable e) {
//        // 返回降级结果
//        return new AppResponse("服务暂时不可用，请稍后重试");
//    }
//
//    private boolean shouldDegrade(Throwable e) {
//        // 根据异常类型判断是否触发降级
//        return e instanceof RuntimeException || e instanceof TimeoutException;
//    }
//
//}
