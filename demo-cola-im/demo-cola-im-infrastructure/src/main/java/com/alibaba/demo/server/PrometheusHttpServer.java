package com.alibaba.demo.server;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PrometheusHttpServer {

    private final PrometheusMeterRegistry prometheusRegistry;

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    public PrometheusHttpServer(PrometheusMeterRegistry prometheusRegistry) {
        this.prometheusRegistry = prometheusRegistry;
    }

    public void start(int port) throws InterruptedException {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(65536))
                                .addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
                                        if ("/prometheus".equals(req.uri())) {
                                            byte[] content = prometheusRegistry.scrape().getBytes();
                                            FullHttpResponse response = new DefaultFullHttpResponse(
                                                    req.protocolVersion(),
                                                    io.netty.handler.codec.http.HttpResponseStatus.OK,
                                                    ctx.alloc().buffer().writeBytes(content));
                                            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; version=0.0.4");
                                            ctx.writeAndFlush(response);
                                        } else {
                                            ctx.writeAndFlush(new DefaultFullHttpResponse(
                                                    req.protocolVersion(),
                                                    io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND));
                                        }
                                    }
                                });
                    }
                });

        b.bind(port).sync();
        log.info("Prometheus HTTP server started on port:{}", port);
    }

    public void stop() {
        if (boss != null) boss.shutdownGracefully();
        if (worker != null) worker.shutdownGracefully();
        log.info("Prometheus HTTP server stopped");
    }

    @PreDestroy
    public void destroy() {
        stop();
    }
}
