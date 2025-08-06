package com.alibaba.demo.zookeeper;

import lombok.Data;

/**
 * 服务端zookeeper协调者（zookeepr协调客户端）
 */
@Data
public class ServerWorker {
	private ServerNode serverNode ;
	/**
	 *饿汉式创建ServerWorker单例
	 */
	private static final ServerWorker instance = new ServerWorker();
	public static ServerWorker instance(){
		return instance;
	}

}
