package com.alibaba.demo.handler;

import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    // 空闲超时次数阈值（可选），例如连续多次空闲才关闭
    private int idleCount = 0;
    private static final int MAX_IDLE_COUNT = 2;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                // 读空闲：表示一段时间内没有收到任何数据（包括心跳和业务消息）
                idleCount++;
                log.warn("Channel {} read idle {} times", ctx.channel().id(), idleCount);
                if (idleCount >= MAX_IDLE_COUNT) {
                    log.warn("Channel {} exceeded max idle count, closing", ctx.channel().id());
                    ctx.close(); // 关闭连接，释放资源
                }
                // 可选：发送一个 ping 给客户端（如果希望服务端主动探测）
                MsgSender.send(ctx.channel(), HelloProto.Heartbeat.newBuilder().setClientTime(System.currentTimeMillis()).build());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HelloProto.MessageWrapper message) {
            if (CmdEnum.HEART.getName().equals(message.getCmd().toLowerCase())) {
                // 收到客户端心跳，重置空闲计数
                idleCount = 0;
                // 响应 pong
                HelloProto.Heartbeat heartbeat = HelloProto.Heartbeat.newBuilder().setClientTime(System.currentTimeMillis()).build();
                MsgSender.send(ctx.channel(), heartbeat);
                log.debug("Received ping from {}, sent pong", ctx.channel().remoteAddress());
                return; // 心跳消息不继续传递到业务处理器
            }
        }
        // 非心跳消息，传递给下一个 handler
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("HeartbeatHandler exception", cause);
        ctx.close();
    }
}
