package com.alibaba.demo.handler;

import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<HelloProto.MessageWrapper> {

    @Autowired
    private MessageDispatcher dispatcher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HelloProto.MessageWrapper msg) throws Exception {
        dispatcher.dispatch(ctx, msg);
    }
}
