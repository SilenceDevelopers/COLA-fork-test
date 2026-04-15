package com.alibaba.demo.listener;

import com.alibaba.demo.constants.Constants;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigChangeListener extends AbstractConfigChangeListener {

    private final Cache<String, Object> caffeineCache;

    @Override
    public void receiveConfigChange(ConfigChangeEvent event) {
        ConfigChangeItem changeItem = event.getChangeItem(Constants.WHITELIST);
        if (changeItem != null) {
            String oldValue = changeItem.getOldValue();
            String newValue = changeItem.getNewValue();
            if (!oldValue.equals(newValue)) {
                log.info("目标配置项已变更: key={}, oldValue={}, newValue={}", "your.target.key", oldValue, newValue);
                List<String> whitelist = List.of(newValue.split(","));
                caffeineCache.put(Constants.CAFFEINE_WHITELIST_KEY, whitelist);
            }
        }
    }
}
