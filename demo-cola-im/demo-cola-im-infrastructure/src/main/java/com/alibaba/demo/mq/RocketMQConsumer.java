package com.alibaba.demo.mq;

import cn.hutool.json.JSONUtil;
import com.alibaba.demo.session.UserChannelManager;
import io.netty.channel.Channel;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.JsonUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

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
    private final UserChannelManager userChannelManager;

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
        defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (messageExt, context) -> {
            for (MessageExt msg : messageExt) {
                byte[] body = msg.getBody();
                String messageContent = new String(body, StandardCharsets.UTF_8);
                handleIncomingMessage(messageContent, msg.getMsgId());

            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        defaultMQPushConsumer.start();
        log.info("RocketMQ consumer started for topic: " + topic);
    }

    private void handleIncomingMessage(String messageContent, String msgId) {
        ImMessage imMsg = JSONUtil.toBean(messageContent, ImMessage.class);
        Long toUserId = imMsg.getToUserId();

        // 从本地 Netty 会话管理器中获取 Channel
        Channel channel = userChannelManager.getChannel(toUserId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(new TextWebSocketFrame(messageContent));
        } else {
            // 用户可能已经断开，或者 Redis 映射脏数据
            // 可以重新查询 Redis 确认用户所在节点，或者存储离线消息
            handleOfflineOrRedirect(imMsg);
        }
    }

    @PreDestroy
    public void destroy() {
        if (defaultMQPushConsumer != null) {
            defaultMQPushConsumer.shutdown();
        }
    }
}
