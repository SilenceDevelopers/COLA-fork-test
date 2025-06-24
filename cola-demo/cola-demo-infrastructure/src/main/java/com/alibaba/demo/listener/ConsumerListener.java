package com.alibaba.demo.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
//@RocketMQMessageListener(consumerGroup = "consumer_test", topic = "consumer_topic")
public class ConsumerListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("消费者收到消息：" + new String(message.getBody()));
    }
}
