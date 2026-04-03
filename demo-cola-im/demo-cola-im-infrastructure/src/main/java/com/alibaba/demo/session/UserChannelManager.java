package com.alibaba.demo.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class UserChannelManager {

    private static final AttributeKey<Long> USER_ID_ATTR = AttributeKey.newInstance("userId");

    private final ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();

    /**
     * 用户上线：添加映射关系，并注册 Channel 关闭时的自动清理
     *
     * @param userId  用户ID
     * @param channel Netty Channel
     */
    public void addChannel(Long userId, Channel channel) {
        channel.attr(USER_ID_ATTR).set(userId);
        Channel existing = userChannelMap.put(userId, channel);
        // 如果同一个 userId 已经存在活跃连接，说明异常，避免多点登录
        if (existing != null && existing.isActive()) {
            log.warn("User {} already has an active channel, closing old one", userId);
            existing.close();
        }
        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            removeChannel(userId);
            log.info("Channel closed, remove user:{}", userId);
        });
        log.info("User {} connected, current local connections: {}", userId, userChannelMap.size());
    }

    /**
     * 用户下线：移除映射关系
     *
     * @param userId 用户ID
     */
    public void removeChannel(Long userId) {
        Channel removed = userChannelMap.get(userId);
        if (removed != null) {
            // 如果 Channel 还活跃，则主动关闭
            if (removed.isActive()) {
                //关闭连接
                removed.close();
                //移除对应的map值
                userChannelMap.remove(userId);
            }
            log.info("User {} disconnected, remaining connections: {}", userId, userChannelMap.size());
        }
    }

    /**
     * 获取用户的 Channel
     *
     * @param userId 用户ID
     * @return 如果用户在此节点上在线，返回 Channel，否则返回 null
     */
    public Channel getChannel(Long userId) {
        return userChannelMap.get(userId);
    }

    /**
     * 判断用户是否在本节点在线
     */
    public boolean isOnline(Long userId) {
        Channel ch = userChannelMap.get(userId);
        return ch != null && ch.isActive();
    }

    /**
     * 获取当前节点上的所有在线用户ID
     */
    public List<Long> getAllOnlineUserIds() {
        return new ArrayList<>(userChannelMap.keySet());
    }

    /**
     * 获取当前节点上的连接总数
     */
    public int getConnectionCount() {
        return userChannelMap.size();
    }

    /**
     * 关闭所有连接（通常在节点优雅关闭时调用）
     */
    public void closeAllConnections() {
        for (Map.Entry<Long, Channel> entry : userChannelMap.entrySet()) {
            Long userId = entry.getKey();
            Channel ch = entry.getValue();
            if (ch.isActive()) {
                ch.close();
            }
            log.info("Closed connection for user: {}", userId);
        }
        userChannelMap.clear();
    }

    /**
     * 根据 Channel 获取绑定的 userId（工具方法）
     *
     * @param channel Netty Channel
     * @return userId 或 null
     */
    public static Long getUserIdFromChannel(Channel channel) {
        return channel.attr(USER_ID_ATTR).get();
    }

}
