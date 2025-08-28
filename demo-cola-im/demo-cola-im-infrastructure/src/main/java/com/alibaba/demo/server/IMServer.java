package com.alibaba.demo.server;

import com.alibaba.demo.constant.Constants;
import com.alibaba.demo.handler.NettyServerHandler;
import com.alibaba.demo.monitor.NettyDirectMemoryMetrics;
import com.alibaba.demo.utils.IOUtil;
import com.alibaba.demo.utils.NodeUtil;
import com.alibaba.demo.zookeeper.ServerNode;
import com.alibaba.demo.zookeeper.ServerRouterWorker;
import com.alibaba.demo.zookeeper.ServerWorker;
import com.alibaba.demo.zookeeper.ZKService;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.FutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private ZKService zkService;

    @Autowired
    private NettyDirectMemoryMetrics nettyDirectMemoryMetrics;

    private PrometheusMeterRegistry prometheusRegistry;

    @Autowired
    private IMServerInitializer imServerInitializer;

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
        b.localAddress(new InetSocketAddress("0.0.0.0", port));
        b.childHandler(imServerInitializer);
        ChannelFuture channelFuture = b.bind().addListener((FutureListener<Void>) future -> {
            if (future.isSuccess()) {
                log.info("IM server started at port: {}", port);
                if (!zkService.checkNodeExists(Constants.MANAGE_PATH)) {
                    zkService.createPersistentNode(Constants.MANAGE_PATH);
                }
                ServerNode serverNode = new ServerNode(IOUtil.getHostAddress(), port);
                String pathRegistered = zkService.createNode(Constants.PATH_PREFIX, serverNode);
                serverNode.setId(NodeUtil.getIdByPath(pathRegistered, Constants.PATH_PREFIX));
                log.info("本地节点, path={}, id={}", pathRegistered, serverNode.getId());
                ServerWorker.instance().setServerNode(serverNode);
                ServerRouterWorker.instance().init();
                exposure();
                nettyDirectMemoryMetrics.init();
            } else {
                log.error("IM server start failed at port: {}!", port);
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

    /**
     * 在Netty服务中暴露Http端口
     */
    private void exposure() {
        // Netty启动完成后，创建PrometheusMeterRegistry并绑定指标
        prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        bindJvmMetrics(prometheusRegistry);
        try {
            // 启动用于暴露指标的HTTP服务
            new PrometheusHttpServer(prometheusRegistry).start(9090);
        } catch (InterruptedException e) {
            log.error("Http Server start error, message:{}", e.getMessage());
        }
    }

    private void bindJvmMetrics(PrometheusMeterRegistry registry) {
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        log.info("JVM metrics bound to PrometheusMeterRegistry");
    }
}
