package com.alibaba.demo;

import com.example.protobuf.HelloProto;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;

import java.io.ByteArrayOutputStream;

public class ProtobufHexBuilder {
    public static void main(String[] args) throws Exception {
        // 构建 LoginRequest
        HelloProto.LoginRequest loginRequest = HelloProto.LoginRequest.newBuilder()
                .setUserId(111L)
                .setToken("abc123")
                .build();

        // 序列化 LoginRequest 得到 body
        byte[] bodyBytes = loginRequest.toByteArray();

        HelloProto.MoveReq moveReq = HelloProto.MoveReq.newBuilder().setX(1).setY(1).build();
        HelloProto.Move move = HelloProto.Move.newBuilder().setReq(moveReq).build();
        byte[] bodyBytes2 = move.toByteArray();

        // 构建 MessageWrapper
        HelloProto.MessageWrapper wrapper = HelloProto.MessageWrapper.newBuilder()
                .setCmd("move")
                .setBody(ByteString.copyFrom(bodyBytes2))
                .build();

        // 序列化 MessageWrapper 为字节数组
        byte[] messageBytes = wrapper.toByteArray();

        // ✅ 加上 Varint32 长度前缀
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CodedOutputStream codedOut = CodedOutputStream.newInstance(outputStream);
        codedOut.writeUInt32NoTag(messageBytes.length); // 写入长度前缀
        codedOut.flush();

        outputStream.write(messageBytes); // 写入实际 protobuf 数据
        byte[] finalPacket = outputStream.toByteArray();

        // 打印为 HEX 字符串（PacketSender 可直接复制使用）
        StringBuilder hex = new StringBuilder();
        for (byte b : finalPacket) {
            hex.append(String.format("%02X", b));
        }

        System.out.println("发送用 HEX 数据：");
        System.out.println(hex.toString().trim());
    }
}
