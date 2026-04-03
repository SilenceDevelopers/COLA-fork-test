package com.alibaba.demo.zookeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZKServiceImpl implements ZKService {

    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public boolean checkNodeExists(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return stat == null;
    }

    @Override
    public String createPersistentNode(String path) throws Exception {
        return curatorFramework.create().creatingParentContainersIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL).forPath(path);
    }

    @Override
    public String createNode(String prefix, ServerNode serverNode) throws Exception {
        byte[] payload = new ObjectMapper().writeValueAsBytes(serverNode);
        return curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(prefix,payload);
    }
}
