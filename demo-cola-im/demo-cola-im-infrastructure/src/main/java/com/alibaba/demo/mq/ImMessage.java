package com.alibaba.demo.mq;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class ImMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 基础标识字段 ====================
    private String msgId;           // 消息唯一ID（全局唯一，用于去重和回调）
    private Long timestamp;         // 消息发送时间戳（毫秒，用于排序和展示）

    // ==================== 收发双方 ====================
    private Long fromUserId;      // 发送者ID
    private Long toUserId;        // 接收者ID（单聊时为目标用户ID，群聊时可为群ID）
    private String fromDeviceId;    // 发送设备ID（可选，用于多端同步）
    private String toDeviceId;      // 目标设备ID（可选，指定设备推送）

    // ==================== 会话信息 ====================
    private String conversationId;  // 会话ID（单聊：userId1_userId2 排序后拼接；群聊：群ID）
    private Integer chatType;       // 会话类型：1-单聊，2-群聊，3-系统通知等

    // ==================== 消息内容 ====================
    private Integer msgType;        // 消息类型：1-文本，2-图片，3-语音，4-视频，5-文件，6-位置，7-自定义
    private String content;         // 消息内容（文本时为纯文本，图片等时为URL或JSON描述）
    private Map<String, Object> extra; // 扩展字段（用于发送表情、引用消息、@某人等）

    // ==================== 状态与控制 ====================
    private Integer needAck;        // 是否需要已读回执：0-不需要，1-需要
    private Integer priority;       // 消息优先级：0-低，1-正常，2-高（可用于优先消费）
    private Long expireTime;        // 消息过期时间戳（超过则丢弃，用于临时消息）

    // ==================== 构造方法 ====================
    public ImMessage() {
        // 自动生成消息ID和时间戳
        this.msgId = generateMsgId();
        this.timestamp = System.currentTimeMillis();
    }

    // 便捷构造：单聊文本消息
    public ImMessage(Long fromUserId, Long toUserId, String content) {
        this();
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.msgType = 1; // 文本
        this.chatType = 1; // 单聊
        this.conversationId = generateConversationId(fromUserId, toUserId);
    }

    // ==================== 辅助方法 ====================
    private String generateMsgId() {
        // 可使用 UUID + 时间戳，或引入分布式ID生成器（如雪花算法）
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String generateConversationId(Long userId1, Long userId2) {
        // 单聊会话ID：将两个userId排序后拼接，保证一致
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }

    // 可增加其他便捷方法，如判断是否过期
    public boolean isExpired() {
        return expireTime != null && System.currentTimeMillis() > expireTime;
    }
}
