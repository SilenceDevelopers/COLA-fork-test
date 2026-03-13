package com.alibaba.demo.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import com.alibaba.demo.sentinel.handler.CustomBlockExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SentinelConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SentinelWebInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public BlockExceptionHandler sentinelBlockExceptionHandler(){
        return new CustomBlockExceptionHandler();
    }

    @Bean
    public SentinelWebMvcConfig sentinelWebMvcConfig(){
        return new SentinelWebMvcConfig();
    }

//    /**
//     * 使用Sentinel原生Provider过滤器
//     */
//    @Bean
//    @ConditionalOnProperty(name = "dubbo.provider.enabled", havingValue = "true", matchIfMissing = true)
//    public Filter sentinelDubboProviderFilter(){
//        return new SentinelDubboProviderFilter();
//    }
//
//    /**
//     * 使用Sentinel原生Consumer过滤器
//     */
//    @Bean
//    @ConditionalOnProperty(name = "dubbo.consumer.enabled", havingValue = "true", matchIfMissing = true)
//    public Filter sentinelDubboConsumerFilter(){
//        return new SentinelDubboConsumerFilter();
//    }

//    @Bean
//    public Filter sentinelDubboFilter(){
//        return new SentinelDubboFilter();
//    }

//    @Bean
//    public DubboFallback dubboFallback(){
//        return new CustomDubboFallback();
//    }

}
