package com.alibaba.demo.server;

import com.alibaba.demo.handler.HeartbeatHandler;
import com.alibaba.demo.handler.NettyServerHandler;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class IMServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("protobufDecoder", new ProtobufDecoder(HelloProto.MessageWrapper.getDefaultInstance()));
        pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast("protobufEncoder", new ProtobufEncoder());
        pipeline.addLast(new IdleStateHandler(200,0,0, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
        pipeline.addLast(nettyServerHandler);
    }
}
