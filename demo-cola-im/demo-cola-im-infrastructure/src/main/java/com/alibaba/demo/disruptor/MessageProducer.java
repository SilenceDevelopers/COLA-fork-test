package com.alibaba.demo.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private RingBuffer<MessageEvent> ringBuffer;

    @Autowired
    public MessageProducer(Disruptor<MessageEvent> disruptor) {
        this.ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(String msg) {
        ringBuffer.publishEvent((event, sequence) -> event.setMessage(msg));
    }
}
