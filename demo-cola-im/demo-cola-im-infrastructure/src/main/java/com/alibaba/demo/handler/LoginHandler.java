package com.alibaba.demo.handler;

import com.alibaba.demo.annotation.MsgHandler;
import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
@MsgHandler(cmd = CmdEnum.LOGIN, message = HelloProto.LoginRequest.class)
public class LoginHandler implements MessageHandler<HelloProto.LoginRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, HelloProto.LoginRequest msg) {
        Long userId = msg.getUserId();
        String token = msg.getToken();

        System.out.println("[LOGIN] userId=" + msg.getUserId());
    }

}
