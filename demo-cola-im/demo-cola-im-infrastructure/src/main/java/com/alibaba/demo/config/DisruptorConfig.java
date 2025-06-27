package com.alibaba.demo.config;

import com.alibaba.demo.annotation.DisruptorHandler;
import com.alibaba.demo.disruptor.MessageEvent;
import com.alibaba.demo.disruptor.MessageEventFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Configuration
public class DisruptorConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public Disruptor<MessageEvent> disruptor(MessageEventFactory factory) {

        Disruptor<MessageEvent> disruptor = new Disruptor<>(
                factory,
                1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        // 获取所有带注解的 handler
        Map<String, Object> beans = context.getBeansWithAnnotation(DisruptorHandler.class);
        List<HandlerWrapper> handlers = new ArrayList<>();

        for (Object bean : beans.values()) {
            Class<?> clazz = AopUtils.getTargetClass(bean);
            DisruptorHandler annotation = clazz.getAnnotation(DisruptorHandler.class);
            handlers.add(new HandlerWrapper((EventHandler<MessageEvent>) bean, annotation.order(), annotation.parallel()));
        }

        // 分组 handler（按 order 分组）
        Map<Integer, List<HandlerWrapper>> groupedHandlers = handlers.stream()
                .collect(Collectors.groupingBy(h -> h.order));

        List<Integer> sortedOrders = groupedHandlers.keySet().stream().sorted().toList();

        EventHandlerGroup<MessageEvent> prevGroup = null;

        for (Integer order : sortedOrders) {
            List<HandlerWrapper> group = groupedHandlers.get(order);
            EventHandler<MessageEvent>[] eventHandlers = group.stream().map(h -> h.handler).toArray(EventHandler[]::new);

            if (prevGroup == null) {
                // 第一个消费组
                prevGroup = disruptor.handleEventsWith(eventHandlers);
            } else {
                // 后续消费组
                prevGroup = prevGroup.then(eventHandlers);
            }
        }

        disruptor.start();
        return disruptor;
    }

    static class HandlerWrapper {
        EventHandler<MessageEvent> handler;
        int order;
        boolean parallel;

        HandlerWrapper(EventHandler<MessageEvent> handler, int order, boolean parallel) {
            this.handler = handler;
            this.order = order;
            this.parallel = parallel;
        }
    }

    @Bean
    public MessageEventFactory factory() {
        return new MessageEventFactory();
    }
}
