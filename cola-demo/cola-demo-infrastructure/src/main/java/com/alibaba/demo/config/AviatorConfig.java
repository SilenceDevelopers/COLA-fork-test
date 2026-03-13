package com.alibaba.demo.config;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AviatorConfig {

    @Bean
    public AviatorEvaluatorInstance getAviatorEvaluator(AviatorProperties aviatorProperties) {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

        instance.setCachedExpressionByDefault(aviatorProperties.isCacheEnabled());
        instance.useLRUExpressionCache(aviatorProperties.getCacheSize());
        instance.setOption(Options.EVAL_TIMEOUT_MS, aviatorProperties.getTimeoutMs());

        aviatorProperties.getFunctions().forEach(className -> {
            try {
                Class<?> clazz = Class.forName(className);
                if (AbstractFunction.class.isAssignableFrom(clazz)) {
                    instance.addFunction((AbstractFunction) clazz.getDeclaredConstructor().newInstance());
                }
            } catch (Exception e) {
                log.info("执行方法未找到,{}", e.getMessage());
            }
        });
        return instance;
    }
}
