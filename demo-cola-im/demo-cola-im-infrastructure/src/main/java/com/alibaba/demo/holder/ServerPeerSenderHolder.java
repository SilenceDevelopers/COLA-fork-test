package com.alibaba.demo.holder;

import com.alibaba.demo.config.ServerPeerSender;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerPeerSenderHolder {

    private static final Map<Long, ServerPeerSender> SERVER_PEER_SENDER_MAP = new ConcurrentHashMap<>();

    public static void addWorker(Long id, ServerPeerSender serverPeerSender) {
        SERVER_PEER_SENDER_MAP.put(id, serverPeerSender);
    }

    public static ServerPeerSender getWorker(Long id) {
        return SERVER_PEER_SENDER_MAP.get(id);
    }

    public static void removeWorker(Long id) {
        SERVER_PEER_SENDER_MAP.remove(id);
    }

    public static Map<Long, ServerPeerSender> getMap() {
        return SERVER_PEER_SENDER_MAP;
    }
}
