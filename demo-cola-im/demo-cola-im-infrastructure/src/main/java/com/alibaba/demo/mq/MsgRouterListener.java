package com.alibaba.demo.mq;

import cn.hutool.json.JSONUtil;
import com.alibaba.demo.session.UserChannelManager;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MsgRouterListener implements MessageListenerConcurrently {

    private final UserChannelManager userChannelManager;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt msg : list) {
            byte[] body = msg.getBody();
            String messageContent = new String(body, StandardCharsets.UTF_8);
            handleIncomingMessage(messageContent, msg.getMsgId());

        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
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

    private void handleOfflineOrRedirect(ImMessage imMessage){

    }
}
