package com.alibaba.demo.zookeeper;

import com.alibaba.demo.utils.SpringContextUtil;
import org.apache.curator.framework.CuratorFramework;

public class CuratorZKClient {
    private static CuratorFramework singleton = null;
    public static CuratorFramework getSingleton()
    {
        if (null == singleton)
        {
            singleton = SpringContextUtil.getBean(CuratorFramework.class);
        }
        return singleton;
    }
}
