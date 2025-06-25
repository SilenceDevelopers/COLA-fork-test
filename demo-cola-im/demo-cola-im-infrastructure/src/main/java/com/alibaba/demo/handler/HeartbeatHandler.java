package com.alibaba.demo.handler;

import com.alibaba.demo.annotation.MsgHandler;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
@MsgHandler(cmd = "heart", message = HelloProto.Heartbeat.class)
public class HeartbeatHandler implements MessageHandler<HelloProto.Heartbeat>{
    @Override
    public void handle(ChannelHandlerContext ctx, HelloProto.Heartbeat msg) {
        System.out.println("[HEARTBEAT] time=" + msg.getClientTime());
    }

}
