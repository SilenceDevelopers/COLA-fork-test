package com.alibaba.demo.disruptor.handler;

import com.alibaba.demo.annotation.DisruptorHandler;
import com.alibaba.demo.disruptor.AbstractDisruptorHandler;
import com.alibaba.demo.disruptor.MessageEvent;

@DisruptorHandler(order = 2,parallel = true)
public class HandlerB extends AbstractDisruptorHandler {
    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("[B] 记录日志：" + event.getMessage());
    }
}
