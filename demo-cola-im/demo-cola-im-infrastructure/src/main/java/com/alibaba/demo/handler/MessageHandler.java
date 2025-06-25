package com.alibaba.demo.handler;

import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler<T> {

    void handle(ChannelHandlerContext ctx, T msg);

}
