package com.alibaba.demo.disruptor.handler;

import com.alibaba.demo.annotation.DisruptorHandler;
import com.alibaba.demo.disruptor.AbstractDisruptorHandler;
import com.alibaba.demo.disruptor.MessageEvent;

@DisruptorHandler(order = 1, parallel = true)
public class HandlerA extends AbstractDisruptorHandler {
    @Override
    public void onEvent(MessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("[A] 处理消息：" + event.getMessage());
    }
}
