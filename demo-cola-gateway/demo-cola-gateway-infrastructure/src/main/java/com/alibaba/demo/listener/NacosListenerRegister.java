package com.alibaba.demo.listener;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.demo.constants.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class NacosListenerRegister implements ApplicationRunner {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.cloud.nacos.config.file-extension:yml}")
    private String fileExtension;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String group;

    private final NacosConfigManager nacosConfigManager;

    private final ConfigChangeListener configChangeListener;

    private final Cache<String, Object> caffeineCache;

    private final ThreadPoolTaskExecutor dynamicExecutor;

    @Override
    public void run(ApplicationArguments args) {
        String dataId = appName + "." + fileExtension;
        try {
            ConfigService configService = nacosConfigManager.getConfigService();
            cacheWhitelist(configService, dataId);
            configService.addListener(dataId, group, configChangeListener);
            log.info("成功为 dataId: {} 注册 Nacos 监听器", dataId);
        } catch (NacosException e) {
            log.error("注册 Nacos 监听器失败, dataId: {}", dataId, e);
        }
    }

    private void cacheWhitelist(ConfigService configService, String dataId) {
        dynamicExecutor.execute(() -> {
            try {
                String config = configService.getConfig(dataId, group, 3000);
                Map<String, String> map = new Yaml().load(config);
                String str = Objects.toString(getByPath(map, Constants.WHITELIST));
                List<String> whitelist = List.of(str.split(","));
                caffeineCache.put(Constants.CAFFEINE_WHITELIST_KEY, whitelist);
            } catch (NacosException e) {
                e.printStackTrace();
                log.error("白名单缓存初始化异常：{}", e.getErrMsg());
            }

        });
    }

    public static Object getByPath(Map<String, ?> map, String path) {
        int start = 0;
        int end;
        Object current = map;
        while ((end = path.indexOf('.', start)) != -1) {
            String key = path.substring(start, end);
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
                if (current == null) return null;
            } else {
                return null;
            }
            start = end + 1;
        }
        String lastKey = path.substring(start);
        return current instanceof Map ? ((Map<?, ?>) current).get(lastKey) : null;
    }
}
