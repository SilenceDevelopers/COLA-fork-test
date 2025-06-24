package com.alibaba.demo.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "socketio")
@Data
public class SocketIOConfig {

    private String host;

    private Integer port;

    private int bossCount;

    private int workCount;

    private boolean allowCustomRequests;

    private int upgradeTimeout;

    private int pingTimeout;

    private int pingInterval;

    private String[] namespaces;

    @Bean
    public SocketIOServer socketIOServer() {
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setSocketConfig(socketConfig);
        config.setHostname(host);
        config.setPort(port);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);

        //服务端
        final SocketIOServer server = new SocketIOServer(config);

        //添加命名空间（如果你不需要命名空间，下面的代码可以去掉）
        Optional.ofNullable(namespaces).ifPresent(nss ->
                Arrays.stream(nss).forEach(server::addNamespace));

        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {

        return new SpringAnnotationScanner(socketIOServer());
    }

}
