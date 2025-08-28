package com.alibaba.demo.handler;

import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<HelloProto.MessageWrapper> {

    @Autowired
    private MessageDispatcher dispatcher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HelloProto.MessageWrapper msg) throws Exception {
        String cmd = msg.getCmd();
        if (cmd.equals(CmdEnum.HEART.getName())) {
            log.info("收到心跳，clientId={}", msg.getClientId());
            HelloProto.Heartbeat heartbeat = HelloProto.Heartbeat.newBuilder().setClientTime(System.currentTimeMillis()).build();
            MsgSender.send(ctx.channel(), heartbeat);
            return;
        }
        dispatcher.dispatch(ctx, msg);
    }
}
