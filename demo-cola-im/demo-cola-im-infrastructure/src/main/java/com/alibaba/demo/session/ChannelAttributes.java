package com.alibaba.demo.session;

import akka.actor.typed.ActorRef;
import com.alibaba.demo.akka.PlayerMessage;
import io.netty.util.AttributeKey;

public class ChannelAttributes {

    private ChannelAttributes(){}

    public static final AttributeKey<ActorRef<PlayerMessage.Command>> PLAYER_ACTOR = AttributeKey.newInstance("playerActor");
}
