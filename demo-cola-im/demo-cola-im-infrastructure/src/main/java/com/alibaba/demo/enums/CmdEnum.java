package com.alibaba.demo.enums;

import com.example.protobuf.HelloProto;
import com.google.protobuf.GeneratedMessageV3;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CmdEnum {

    HEART(HelloProto.Cmd.HEARTBEAT.name().toLowerCase(), HelloProto.Cmd.HEARTBEAT.getNumber(), HelloProto.Heartbeat.class),
    LOGIN(HelloProto.Cmd.LOGIN.name().toLowerCase(), HelloProto.Cmd.LOGIN.getNumber(), HelloProto.LoginRequest.class),
    CHAT(HelloProto.Cmd.CHAT.name().toLowerCase(),HelloProto.Cmd.CHAT.getNumber(),HelloProto.ChatMessage.class),
    CONNECTED(HelloProto.Cmd.CONNECTED.name().toLowerCase(), HelloProto.Cmd.CONNECTED.getNumber(),HelloProto.ServerPeerConnected.class);

    private final String name;
    private final int protoCode;
    private final Class<? extends GeneratedMessageV3> messageClass;

    CmdEnum(String name, int protoCode, Class<? extends GeneratedMessageV3> messageClass) {
        this.name = name;
        this.protoCode = protoCode;
        this.messageClass = messageClass;
    }

    private static final Map<Class<?>, String> map = new HashMap<>();

    static {
        for (CmdEnum cmdEnum : CmdEnum.values()){
            map.put(cmdEnum.messageClass,cmdEnum.getName());
        }
    }

    public static Map<Class<?>, String> getMap(){
        return map;
    }

    public static String getCmd(Class<?> clazz){
        return map.get(clazz);
    }
}
