package com.alibaba.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class DynamicThreadPoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return dynamicThreadPoolExecutor();
    }

    /**
     * 获取CPU核心数（可用于计算线程数）
     */
    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();

    /**
     * 根据任务类型调整系数：
     * - CPU密集型：建议 coreSize = CPU核数 + 1
     * - IO密集型：建议 coreSize = CPU核数 * 2 （或更多）
     * 这里以混合型为例，采用 CPU核数 * 2
     */
    private static final int FACTOR = 2;

    @Bean("dynamicExecutor")
    public ThreadPoolTaskExecutor dynamicThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数 = CPU核数 * 系数
        int corePoolSize = CPU_CORE * FACTOR;
        // 最大线程数 = 核心线程数 * 2 （也可根据情况调整）
        int maxPoolSize = corePoolSize * 2;
        // 队列容量：适当大一些，但避免内存溢出，常用 200~500
        int queueCapacity = 200;
        // 线程空闲存活时间（秒）
        int keepAliveSeconds = 60;

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("dynamic-pool-");

        // 拒绝策略：由调用线程执行（保证任务不丢失）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 优雅关闭：等待所有任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("线程池已初始化: 核心线程数={}, 最大线程数={}, CPU核数={}", corePoolSize, maxPoolSize, CPU_CORE);

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            // 记录日志或发送告警
            log.info("异步方法：{}, 执行异常：{}", method.getName(), ex.getMessage());
        };
    }
}
