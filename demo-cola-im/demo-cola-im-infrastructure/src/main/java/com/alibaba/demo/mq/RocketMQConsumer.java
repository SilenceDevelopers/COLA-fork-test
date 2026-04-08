package com.alibaba.demo.mq;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class RocketMQConsumer {

    @Value("${rocketmq.name-server:127.0.0.1:9876}")
    private String nameServer;

    private static final String IM_TOPIC_PREFIX = "IM_TOPIC_";

    @Value("${im.node.id}")
    private String nodeId;

    private DefaultMQPushConsumer defaultMQPushConsumer;
    private final MsgRouterListener msgRouterListener;

    public void init() throws Exception {
        if (!StringUtils.hasText(nodeId)) {
            throw new Exception("nodeId为空");
        }
        String consumerGroup = "im-consumer-group-" + nodeId;
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(nameServer);

        String topic = IM_TOPIC_PREFIX + nodeId;
        defaultMQPushConsumer.subscribe(topic, "*");
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        defaultMQPushConsumer.registerMessageListener(msgRouterListener);
        defaultMQPushConsumer.start();
        log.info("RocketMQ consumer started for topic: " + topic);
    }

    @PreDestroy
    public void destroy() {
        if (defaultMQPushConsumer != null) {
            defaultMQPushConsumer.shutdown();
        }
    }
}
