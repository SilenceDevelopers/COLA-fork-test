package com.alibaba.demo.listener;

import com.alibaba.demo.constants.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllowedOriginService {

    private final Cache<String, Object> caffeineCache;

    public boolean isAllowedOrigin(String origin) {
        Object allowed = caffeineCache.getIfPresent(Constants.WHITELIST);
        if (allowed != null) {
            if (allowed instanceof List<?>) {
                return ((List<?>) allowed).contains(origin);
            } else {
                return origin.equals(allowed);
            }
        }
        return false;
    }

}

