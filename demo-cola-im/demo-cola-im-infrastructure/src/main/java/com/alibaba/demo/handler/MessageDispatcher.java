package com.alibaba.demo.handler;

import com.alibaba.demo.annotation.MsgHandler;
import com.alibaba.demo.enums.CmdEnum;
import com.example.protobuf.HelloProto;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MessageDispatcher implements ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, MessageHandler<?>> handlers = new HashMap<>();
    private final static Map<String, Parser<?>> parserMap = new HashMap<>();

    public void dispatch(ChannelHandlerContext ctx, HelloProto.MessageWrapper wrapper) throws Exception {
        String cmd = wrapper.getCmd();
        Parser<?> parser = parserMap.get(cmd);
        MessageHandler<?> handler = handlers.get(cmd);
        if (parser != null && handler != null) {
            Object message = parser.parseFrom(wrapper.getBody());
            invokeHandler(ctx, handler, message);
        } else {
            log.warn("未知的消息类型：{}", cmd);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void invokeHandler(ChannelHandlerContext ctx, MessageHandler<?> handler, Object msg) {
        ((MessageHandler<T>) handler).handle(ctx, (T) msg);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) throws BeansException {
        Map<String, Object> beans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(MsgHandler.class);
        for (Object bean : beans.values()) {
            MsgHandler annotation = bean.getClass().getAnnotation(MsgHandler.class);
            CmdEnum cmd = annotation.cmd();
            Class<? extends MessageLite> msgClass = annotation.message();

            try {
                Method method = msgClass.getDeclaredMethod("parser");
                Parser<?> parser = (Parser<?>) method.invoke(null);

                handlers.put(cmd.getName(), (MessageHandler<?>) bean);
                parserMap.put(cmd.getName(), parser);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get parser for: " + msgClass.getName(), e);
            }
        }
    }

}
