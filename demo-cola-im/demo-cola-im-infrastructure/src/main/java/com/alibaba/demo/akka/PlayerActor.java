package com.alibaba.demo.akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.alibaba.demo.domain.PlayerState;
import com.alibaba.demo.handler.MsgSender;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerActor extends AbstractBehavior<PlayerMessage.Command> {

    private final Long playerId;
    private ChannelHandlerContext channel;
    private PlayerState state;

    private PlayerActor(ActorContext<PlayerMessage.Command> context, Long playerId) {
        super(context);
        this.playerId = playerId;
        this.state = new PlayerState(playerId);
    }

    public static Behavior<PlayerMessage.Command> create(Long playerId) {
        return Behaviors.setup(context -> new PlayerActor(context, playerId));
    }

    @Override
    public Receive<PlayerMessage.Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(PlayerMessage.Login.class, this::onLogin)
                .onMessage(PlayerMessage.Move.class, this::onMove)
//                .onMessage(PlayerMessage.Logout.class, this::onLogout)
                .build();
    }

    private Behavior<PlayerMessage.Command> onLogin(PlayerMessage.Login login) {
        this.channel = login.ctx;
        log.info("Player {} logged in", playerId);
        // 发送欢迎消息
        HelloProto.LoginRequest req = HelloProto.LoginRequest.newBuilder()
                .setUserId(playerId)
                .setToken("Welcome " + playerId + "!")
                .build();
        MsgSender.send(channel.channel(), req);
        return this;
    }

    private Behavior<PlayerMessage.Command> onMove(PlayerMessage.Move move) {
        if (channel == null) {
            log.warn("Player {} not logged in", playerId);
            return this;
        }
        // 更新状态
        state.setX(move.x);
        state.setY(move.y);
        log.info("Player {} moved to ({}, {})", playerId, move.x, move.y);
        HelloProto.MoveResp moveResp = HelloProto.MoveResp.newBuilder()
                .setPlayerId(playerId)
                .setX(move.x)
                .setY(move.y)
                .build();
        HelloProto.Move move1 = HelloProto.Move.newBuilder()
                .setResp(moveResp)
                .build();
        // 发送位置响应
//        HelloProto.MessageWrapper resp = HelloProto.MessageWrapper.newBuilder()
//                .setCmd(HelloProto.Cmd.MOVE.name())
//                .setBody(move)
//                .build();
//        channel.writeAndFlush(resp);
        MsgSender.send(channel.channel(), move1);
        return this;
    }

//    private Behavior<PlayerMessage.Command> onLogout(PlayerMessage.Logout logout) {
//        log.info("Player {} logged out", playerId);
//        if (channel != null) {
//            channel.close();
//            channel = null;
//        }
//        return Behaviors.stopped();
//    }
}
