package com.alibaba.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketIOServiceHandler {
//
//    @Autowired
//    private SocketIOConfig socketIOConfig;
//
//    @Autowired
//    private SocketIOServer socketIOServer;
//
//    @OnEvent("testHandler")
//    public void testHandler(SocketIOClient socketIOClient, String data, AckRequest ackRequest) throws JsonProcessingException {
//        log.info("MyTestHandler:{}, from:{}",data, socketIOClient.getSessionId());
//
//        if(ackRequest.isAckRequested()){
//            //返回给客户端，说我接收到了
//            ackRequest.sendAckData("MyTestHandler",data);
//        }
//        socketIOServer.getNamespace("/test").getClient(socketIOClient.getSessionId()).sendEvent("bbbb", "点对点消息的返回" + Math.random());
//    }
}
