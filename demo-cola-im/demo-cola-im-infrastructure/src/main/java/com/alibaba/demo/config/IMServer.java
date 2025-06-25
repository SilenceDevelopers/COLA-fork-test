package com.alibaba.demo.config;

import com.alibaba.demo.handler.NettyServerHandler;
import com.alibaba.demo.utils.IOUtil;
import com.example.protobuf.HelloProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.concurrent.FutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Data
@ConfigurationProperties(prefix = "netty.config")
@Slf4j
public class IMServer {

    private int port;

    private boolean useLinuxNativeEpoll;

    private int bossCount;

    private int workCount;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Class<? extends ServerChannel> serverChannel;

    private NettyServerHandler nettyServerHandler;

    public void run() {
        initGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workGroup);
        b.channel(serverChannel);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.option(ChannelOption.SO_BACKLOG, 128);
        b.childOption(ChannelOption.TCP_NODELAY, true);
        b.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
        b.localAddress(new InetSocketAddress(IOUtil.getHostAddress(), port));
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                pipeline.addLast("protobufDecoder", new ProtobufDecoder(HelloProto.MessageWrapper.getDefaultInstance()));
                pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                pipeline.addLast("dispatcher", nettyServerHandler);
            }
        });
        ChannelFuture channelFuture = b.bind().addListener((FutureListener<Void>) future -> {
            if (future.isSuccess()) {
                log.info("SocketIO server started at port: {}", port);
            } else {
                log.error("SocketIO server start failed at port: {}!", port);
            }
        });
    }

    private void initGroup() {
        if (useLinuxNativeEpoll) {
            bossGroup = new EpollEventLoopGroup(bossCount);
            workGroup = new EpollEventLoopGroup(workCount);
            serverChannel = EpollServerSocketChannel.class;
        } else {
            bossGroup = new NioEventLoopGroup(bossCount);
            workGroup = new NioEventLoopGroup(workCount);
            serverChannel = NioServerSocketChannel.class;
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workGroup.shutdownGracefully().syncUninterruptibly();
    }
}
