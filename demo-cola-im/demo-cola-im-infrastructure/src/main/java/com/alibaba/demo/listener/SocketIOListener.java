package com.alibaba.demo.listener;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.demo.config.IMServer;
import com.alibaba.demo.utils.IOUtil;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SocketIOListener implements CommandLineRunner, DisposableBean {

    //    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private IMServer imServer;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired
    private NamingService namingService;

    @Override
    public void run(String... args) throws Exception {
//        socketIOServer.getNamespace("/test").addListeners(SocketIOServiceHandler.class);
        imServer.run();
        log.info("================= socket 启动成功 ================");
        registerNamingService("Netty-Server", imServer.getPort());
    }

    @Override
    public void destroy() throws Exception {
//        socketIOServer.stop();
        log.info("================= socket 关闭成功 =================");
    }

    @PreDestroy
    public void shutdown() throws Exception {
        imServer.stop();
        log.info("================= socket 关闭成功 =================");
    }

    /**
     * 注册到 nacos 服务中
     *
     * @param nettyName netty服务名称
     * @param nettyPort netty服务端口
     */
    private void registerNamingService(String nettyName, Integer nettyPort) {
        try {
            log.info("-------------- register socket server  {}  {}", nettyName, nettyPort);
            // 注册到nacos
            Instance instance = new Instance();
            instance.setIp(IOUtil.getHostAddress());
            instance.setPort(nettyPort);
            instance.setServiceName(nettyName);
            instance.setWeight(1.0);
            Map<String, String> map = new HashMap<>();
            map.put("preserved.register.source", "SPRING_CLOUD");
            map.put("protocol", "netty");
            instance.setMetadata(map);
            namingService.registerInstance(nettyName, instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
