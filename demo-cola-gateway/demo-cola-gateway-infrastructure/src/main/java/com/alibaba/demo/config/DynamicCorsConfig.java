package com.alibaba.demo.config;

import com.alibaba.demo.listener.AllowedOriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsProcessor;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DynamicCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration defaultConfig = new CorsConfiguration();
        //请求方法
        defaultConfig.addAllowedMethod(HttpMethod.POST.name());
        defaultConfig.addAllowedMethod(HttpMethod.GET.name());
        defaultConfig.addAllowedMethod(HttpMethod.DELETE.name());
        defaultConfig.addAllowedMethod(HttpMethod.PUT.name());
        defaultConfig.addAllowedMethod(HttpMethod.OPTIONS.name());
        //请求头
        defaultConfig.addAllowedHeader("*");
        //携带凭证
        defaultConfig.setAllowCredentials(true);
        //请求缓存时间
        defaultConfig.setMaxAge(3600L);

        //注册到路由匹配规则
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", defaultConfig);

        return new CorsWebFilter(source, new DynamicCorsProcessor());
    }

    private final AllowedOriginService allowedOriginService;

    public class DynamicCorsProcessor implements CorsProcessor {

        private final DefaultCorsProcessor defaultProcessor = new DefaultCorsProcessor();

        @Override
        public boolean process(CorsConfiguration config, ServerWebExchange exchange) {
            ServerHttpRequest request = exchange.getRequest();
            String origin = request.getHeaders().getOrigin();
            // 非跨域请求，直接放行
            if (origin == null) {
                return true;
            }

            boolean isAllowed = allowedOriginService.isAllowedOrigin(origin);
            if (isAllowed) {
                CorsConfiguration dynamicConfig = new CorsConfiguration(config);
                dynamicConfig.setAllowedOrigins(List.of(origin));
                return defaultProcessor.process(dynamicConfig, exchange);
            } else {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }
        }
    }
}
