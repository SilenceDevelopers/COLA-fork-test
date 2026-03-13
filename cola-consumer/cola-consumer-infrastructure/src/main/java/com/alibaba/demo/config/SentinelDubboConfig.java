package com.alibaba.demo.config;

import com.alibaba.csp.sentinel.adapter.dubbo.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.adapter.dubbo.fallback.DubboFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.demo.response.FallbackResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SentinelDubboConfig implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        initSentinelFallback();
    }

    private void initSentinelFallback() {
        // 配置统一的降级处理器
        DubboAdapterGlobalConfig.setConsumerFallback(createConsumerFallback());
        DubboAdapterGlobalConfig.setProviderFallback(createProviderFallback());
    }

    private DubboFallback createConsumerFallback() {
        return new DubboFallback() {
            @Override
            public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
                // 获取服务接口和方法名
                String service = invoker.getInterface().getSimpleName();
                String method = invocation.getMethodName();

                // 记录日志
                log.warn("Dubbo服务调用被限流: {}.{}, 原因: {}", service, method, ex.getClass().getSimpleName());

                // 返回统一的降级响应
                FallbackResponse response = new FallbackResponse(429, String.format("服务%s.%s暂时不可用", service, method));
                return AsyncRpcResult.newDefaultAsyncResult(response, invocation);
            }
        };
    }

    private DubboFallback createProviderFallback() {
        return new DubboFallback() {
            @Override
            public Result handle(Invoker<?> invoker, Invocation invocation, BlockException ex) {
                String service = invoker.getInterface().getSimpleName();
                String method = invocation.getMethodName();

                log.warn("Dubbo服务提供方被限流: {}.{}, 原因: {}", service, method, ex.getClass().getSimpleName());
                FallbackResponse response = new FallbackResponse(503, "服务繁忙，请稍后重试");
                return AsyncRpcResult.newDefaultAsyncResult(response, invocation);
            }
        };
    }
}
