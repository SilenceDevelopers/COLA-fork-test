package com.alibaba.demo.zookeeper;

public interface ZKService {
    boolean checkNodeExists(String path) throws Exception;

    String createPersistentNode(String path) throws Exception;

    String createNode(String prefix , ServerNode serverNode) throws Exception;
}
