package com.alibaba.demo.handler;

import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

public class MsgSender {
    public static void send(Channel channel, GeneratedMessageV3 message) {
        String cmd = CmdEnum.getCmd(message.getClass());
        if (StringUtils.isEmpty(cmd)) throw new IllegalArgumentException("未注册 cmd");

        byte[] body = message.toByteArray();
        HelloProto.MessageWrapper wrapper = HelloProto.MessageWrapper.newBuilder()
                .setCmd(cmd)
                .setBody(ByteString.copyFrom(body))
                .build();

        channel.writeAndFlush(wrapper);
    }
}
