package com.alibaba.demo.akka;

import io.netty.channel.ChannelHandlerContext;

public interface PlayerMessage {

    interface Command{}

    class Move implements Command{
        public final int x;
        public final int y;
        public Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Login implements Command {
        public final ChannelHandlerContext ctx;
        public final Long playerId;
        public Login(ChannelHandlerContext ctx, Long playerId) {
            this.ctx = ctx;
            this.playerId = playerId;
        }
    }
}
