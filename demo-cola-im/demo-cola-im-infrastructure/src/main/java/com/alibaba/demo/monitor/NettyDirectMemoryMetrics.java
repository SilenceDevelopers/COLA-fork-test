package com.alibaba.demo.monitor;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.netty.util.internal.PlatformDependent;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class NettyDirectMemoryMetrics {
    private static final int _1K = 1024;

    private final AtomicLong directMemoryInKB = new AtomicLong();

    // Spring 管理的任务调度器
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    private ScheduledFuture<?> scheduledFuture;

    public NettyDirectMemoryMetrics(MeterRegistry meterRegistry) {
        // 注册 Micrometer Gauge 指标
        Gauge.builder("netty.direct.memory.kb", directMemoryInKB, AtomicLong::get)
                .description("Netty direct memory usage in KB")
                .baseUnit("kilobytes")
                .register(meterRegistry);
    }

    public void init() {
        log.info("Initializing Netty Direct Memory monitor...");

        // 每秒更新一次内存占用
        scheduler.scheduleAtFixedRate(this::updateMemoryUsage, Duration.ofSeconds(1));
    }

    private void updateMemoryUsage() {
        try {
            long usedBytes = getUsedDirectMemory();
            directMemoryInKB.set(usedBytes / _1K);
        } catch (Throwable e) {
            log.warn("Failed to get Netty direct memory usage", e);
        }
    }

    private static long getUsedDirectMemory() {
        try {
            // 优先用官方 API
            return PlatformDependent.usedDirectMemory();
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // 老版本兼容 - 反射获取
            try {
                var field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
                field.setAccessible(true);
                return ((java.util.concurrent.atomic.AtomicLong) field.get(null)).get();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to access Netty direct memory counter", ex);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down Netty Direct Memory monitor...");
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        scheduler.shutdown();
    }
}
