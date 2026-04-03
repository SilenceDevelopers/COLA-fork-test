package com.alibaba.demo.handler;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Props;
import com.alibaba.demo.akka.PlayerActor;
import com.alibaba.demo.akka.PlayerMessage;
import com.alibaba.demo.annotation.MsgHandler;
import com.alibaba.demo.enums.CmdEnum;
import com.alibaba.demo.session.ChannelAttributes;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@MsgHandler(cmd = CmdEnum.LOGIN, message = HelloProto.LoginRequest.class)
public class LoginHandler implements MessageHandler<HelloProto.LoginRequest> {

    @Autowired
    private ActorSystem<Void> actorSystem;

    @Override
    public void handle(ChannelHandlerContext ctx, HelloProto.LoginRequest msg) {
        Long userId = msg.getUserId();
        String token = msg.getToken();

        System.out.println("[LOGIN] userId=" + msg.getUserId());

        Long playerId = msg.getUserId();
        ActorRef<PlayerMessage.Command> playerActor = getOrCreatePlayerActor(playerId);
        ctx.channel().attr(ChannelAttributes.PLAYER_ACTOR).set(playerActor);
        playerActor.tell(new PlayerMessage.Login(ctx, playerId));
    }

    private ActorRef<PlayerMessage.Command> getOrCreatePlayerActor(Long playerId) {
        // 实际应用中可使用 Akka Cluster Sharding 或缓存池
        // 此处简单演示，通过 actorSystem 创建顶级 Actor（注意路径唯一性）
        return actorSystem.systemActorOf(PlayerActor.create(playerId), "player-" + playerId, Props.empty());
    }
}
