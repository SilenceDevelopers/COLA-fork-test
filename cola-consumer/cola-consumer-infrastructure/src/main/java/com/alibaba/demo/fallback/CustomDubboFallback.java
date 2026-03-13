package com.alibaba.demo.fallback;

import com.alibaba.csp.sentinel.adapter.dubbo.fallback.DubboFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.AppResponse;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;

import java.util.Arrays;

@Slf4j
public class CustomDubboFallback implements DubboFallback {
    @Override
    public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
        String interfaceName = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        Object[] arguments = invocation.getArguments();

        // 记录被限流或降级的调用
        log.warn("Dubbo call blocked: {}.{} with args {}, reason: {}", interfaceName, methodName, Arrays.toString(arguments), ex.getClass().getSimpleName());
        // 根据不同异常类型返回不同的降级结果
        if (ex instanceof FlowException) {
            return handleFlowException(interfaceName, methodName, arguments);
        } else if (ex instanceof DegradeException) {
            return handleDegradeException(interfaceName, methodName, arguments);
        } else if (ex instanceof ParamFlowException) {
            return handleParamFlowException(interfaceName, methodName, arguments);
        } else {
            return handleDefaultException(interfaceName, methodName, arguments);
        }
    }

    private Result handleFlowException(String interfaceName, String methodName, Object[] arguments) {
        // 流控异常处理 - 返回友好提示
        return new AppResponse("服务繁忙，请稍后重试");
    }

    private Result handleDegradeException(String interfaceName, String methodName, Object[] arguments) {
        // 降级异常处理 - 返回降级结果
        // 可以根据接口和方法名返回不同的降级数据
//        if ("getUserInfo".equals(methodName)) {
//            return new AppResponse(getDefaultUserInfo());
//        } else if ("getProductList".equals(methodName)) {
//            return new AppResponse(getEmptyProductList());
//        }
        return new AppResponse("服务暂时不可用");
    }

    private Result handleParamFlowException(String interfaceName, String methodName, Object[] arguments) {
        // 参数流控异常处理
        return new AppResponse("热点参数限流，请稍后重试");
    }

    private Result handleDefaultException(String interfaceName, String methodName, Object[] arguments) {
        // 默认异常处理
        return new AppResponse("系统繁忙，请稍后重试");
    }
}
