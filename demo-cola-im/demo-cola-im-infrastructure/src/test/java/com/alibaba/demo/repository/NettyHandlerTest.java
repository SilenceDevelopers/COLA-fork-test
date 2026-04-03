package com.alibaba.demo.repository;

import com.alibaba.demo.handler.MessageDispatcher;
import com.alibaba.demo.handler.NettyServerHandler;
import com.example.protobuf.HelloProto;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
public class NettyHandlerTest {

    @Mock
    private MessageDispatcher messageDispatcher;

    @InjectMocks
    private NettyServerHandler nettyServerHandler;

    @Test
    public void test() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ProtobufVarint32FrameDecoder(),
                new ProtobufDecoder(HelloProto.MessageWrapper.getDefaultInstance()),
                new ProtobufVarint32LengthFieldPrepender(),
                new ProtobufEncoder(),
                nettyServerHandler);

        long userId = 123L;
        HelloProto.LoginRequest loginRequest = HelloProto.LoginRequest.newBuilder().setUserId(userId).build();
        HelloProto.MessageWrapper messageWrapper = HelloProto.MessageWrapper.newBuilder()
                .setCmd("login")
                .setUserId(userId)
                .setBody(loginRequest.toByteString())
                .build();
        embeddedChannel.writeInbound(messageWrapper);
        Object output = embeddedChannel.readOutbound();
        assertTrue(output instanceof HelloProto.MessageWrapper);
    }
}
