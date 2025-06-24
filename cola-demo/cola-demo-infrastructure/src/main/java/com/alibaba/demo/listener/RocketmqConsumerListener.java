package com.alibaba.demo.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
//@RocketMQMessageListener(consumerGroup = "canal_test", topic = "canal_test", selectorExpression = "data_tag")
public class RocketmqConsumerListener implements RocketMQListener<MessageExt> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void onMessage(MessageExt message) {
        System.out.println("Canal消费者收到消息：" + new String(message.getBody()));
        redisTemplate.opsForValue().set("canalTest", new String(message.getBody()));

    }

}
