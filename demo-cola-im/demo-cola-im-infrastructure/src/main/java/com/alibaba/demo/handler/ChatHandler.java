package com.alibaba.demo.handler;

import com.alibaba.demo.annotation.MsgHandler;
import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
@MsgHandler(cmd = CmdEnum.CHAT, message = HelloProto.ChatMessage.class)
public class ChatHandler implements MessageHandler<HelloProto.ChatMessage> {

    @Override
    public void handle(ChannelHandlerContext ctx, HelloProto.ChatMessage msg) {
        System.out.println("[HEARTBEAT] time=" + msg.getContent());
    }

}
