package com.alibaba.demo.dubbo.api.impl;

import com.alibaba.demo.dubbo.api.ProviderApi;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService
public class ProviderApiImpl implements ProviderApi {
    @Override
    public String sayProviderHello() {
        boolean isProvider = RpcContext.getServiceContext().isProviderSide();
        String clientIp = RpcContext.getServiceContext().getRemoteHost();
        String application = RpcContext.getServiceContext().getUrl().getApplication();
        System.out.println("isProvider:"+isProvider+",clientIp:"+clientIp+",application:"+application);
        return "Dubbo Hello";
    }
}
