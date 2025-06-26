package com.alibaba.demo.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class NacosConfig {

    @Bean
    public NamingService namingService(NacosDiscoveryProperties nacosDiscoveryProperties) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", nacosDiscoveryProperties.getServerAddr());
        properties.setProperty("namespace", nacosDiscoveryProperties.getNamespace());
        properties.setProperty("username", nacosDiscoveryProperties.getUsername());
        properties.setProperty("password", nacosDiscoveryProperties.getPassword());

        return NamingFactory.createNamingService(properties);
    }
}
