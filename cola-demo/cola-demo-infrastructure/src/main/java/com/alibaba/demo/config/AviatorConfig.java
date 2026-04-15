package com.alibaba.demo.config;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AviatorConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public AviatorEvaluatorInstance getAviatorEvaluator(AviatorProperties props) {
        // 1. 获取 Aviator 引擎实例
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

        //一键启用安全沙箱 防止恶意表达式执行 System.exit(0) 等危险操作
        instance.enableSandboxMode();

        // 2. (核心) 开启全局默认缓存模式
        // 从此，compile(script) 和 execute(script, env) 等方法将默认使用缓存，不再需要每次都传 true
        instance.setCachedExpressionByDefault(props.isCacheEnabled());

        // 3. (核心) 启用 LRU 缓存，并设置最大缓存条目数（例如：10000）
        // 这会替换掉默认的无容量限制的 ConcurrentHashMap，防止内存溢出
        instance.useLRUExpressionCache(props.getCacheSize());

        // --- 以下是你原有的安全、超时等配置 ---
        // 安全配置：移除危险函数
        instance.removeFunction("class");
        instance.removeFunction("new");
        // 超时配置：防止恶意或长时间执行的脚本
        instance.setOption(Options.EVAL_TIMEOUT_MS, 3000L);
        // 注册自定义函数（从 Spring 容器中获取 Bean）
        Map<String, AbstractFunction> functionMap = applicationContext.getBeansOfType(AbstractFunction.class);
        functionMap.values().forEach(instance::addFunction);
        return instance;
    }
}
