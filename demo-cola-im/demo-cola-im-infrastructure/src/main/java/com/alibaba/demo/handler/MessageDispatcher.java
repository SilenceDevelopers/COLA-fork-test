package com.alibaba.demo.handler;

import com.alibaba.demo.annotation.MsgHandler;
import com.example.protobuf.HelloProto;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MessageDispatcher implements ApplicationContextAware {

    private final Map<String, MessageHandler<?>> handlers = new HashMap<>();
    private final Map<String, Parser<?>> parserMap = new HashMap<>();

    public void dispatch(ChannelHandlerContext ctx, HelloProto.MessageWrapper wrapper) throws Exception {
        String cmd = wrapper.getCmd();
        Parser<?> parser = parserMap.get(cmd);
        MessageHandler<?> handler = handlers.get(cmd);
        if (parser != null && handler != null) {
            Object message = parser.parseFrom(wrapper.getBody());
            invokeHandler(ctx, handler, message);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void invokeHandler(ChannelHandlerContext ctx, MessageHandler<?> handler, Object msg) {
        ((MessageHandler<T>) handler).handle(ctx, (T) msg);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MsgHandler.class);
        for (Object bean : beans.values()) {
            MsgHandler annotation = bean.getClass().getAnnotation(MsgHandler.class);
            String cmd = annotation.cmd();
            Class<? extends MessageLite> msgClass = annotation.message();

            try {
                Method method = msgClass.getDeclaredMethod("parser");
                Parser<?> parser = (Parser<?>) method.invoke(null);

                handlers.put(cmd, (MessageHandler<?>) bean);
                parserMap.put(cmd, parser);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get parser for: " + msgClass.getName(), e);
            }
        }
    }
}
