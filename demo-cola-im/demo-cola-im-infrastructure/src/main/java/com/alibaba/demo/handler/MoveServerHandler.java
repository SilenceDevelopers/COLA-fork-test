package com.alibaba.demo.handler;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import com.alibaba.demo.akka.PlayerMessage;
import com.alibaba.demo.annotation.MsgHandler;
import com.alibaba.demo.enums.CmdEnum;
import com.alibaba.demo.session.ChannelAttributes;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MsgHandler(cmd = CmdEnum.MOVE, message = HelloProto.Move.class)
@Slf4j
public class MoveServerHandler implements MessageHandler<HelloProto.Move> {

    @Autowired
    private ActorSystem<Void> actorSystem;

    @Override
    public void handle(ChannelHandlerContext ctx, HelloProto.Move msg) {
        ActorRef<PlayerMessage.Command> actor = ctx.channel().attr(ChannelAttributes.PLAYER_ACTOR).get();
        if (actor != null) {
            if (msg.hasReq()) {
                HelloProto.MoveReq moveReq = msg.getReq();
                actor.tell(new PlayerMessage.Move(moveReq.getX(), moveReq.getY()));
            }
        } else {
            sendError(ctx, "Not logged in");
        }
    }

    private void sendError(ChannelHandlerContext ctx, String errorMsg) {
        HelloProto.ErrorMsg error = HelloProto.ErrorMsg.newBuilder()
                .setError(errorMsg)
                .build();
        MsgSender.send(ctx.channel(), error);
    }
}
