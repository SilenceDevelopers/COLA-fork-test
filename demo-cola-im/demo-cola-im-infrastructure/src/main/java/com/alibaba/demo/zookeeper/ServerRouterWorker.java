package com.alibaba.demo.zookeeper;

import com.alibaba.demo.config.ServerPeerSender;
import com.alibaba.demo.holder.ServerPeerSenderHolder;
import com.alibaba.demo.utils.NodeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import static com.alibaba.demo.constant.Constants.MANAGE_PATH;
import static com.alibaba.demo.constant.Constants.PATH_PREFIX;

@Slf4j
public class ServerRouterWorker {

	private static final ServerRouterWorker instance = new ServerRouterWorker();
	public static ServerRouterWorker instance(){
		return instance;
	}

	private boolean inited = false;

	public void init() throws Exception {
		if(inited){
			return;
		}
		CuratorFramework curatorFramework = CuratorZKClient.getSingleton();
		//订阅节点的增加和删除事件
		CuratorCache curatorCache = CuratorCache.build(curatorFramework, MANAGE_PATH, CuratorCache.Options.DO_NOT_CLEAR_ON_CLOSE);
        CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder().forPathChildrenCache(MANAGE_PATH, curatorFramework, (client, event) -> {
            ChildData data = event.getData();
            switch (event.getType()) {
                case CHILD_ADDED:
                    log.info("CHILD_ADDED : " + data.getPath());
                    processAdd(data);
                    break;
                case CHILD_REMOVED:
                    log.info("CHILD_REMOVED : " + data.getPath());
                    break;
                case CHILD_UPDATED:
                    log.info("CHILD_UPDATED : " + data.getPath());
                    break;
                default:
                    log.debug("[PathChildrenCache]节点数据为空, path={}", data == null ? "null" : data.getPath());
                    break;
            }
        }).build();
        curatorCache.listenable().addListener(
                curatorCacheListener, null);
		log.info("Register zk watcher successfully!");
        curatorCache.start();
		this.inited = true ;
	}

	/**
	 * zk节点新增
	 * @param data
	 */
	private void processAdd(ChildData data) {
		ServerPeerSender serverPeerSender = new ServerPeerSender();
		long id = NodeUtil.getIdByPath(data.getPath(), PATH_PREFIX);
		ServerNode serverNode = JSONObject.parseObject(data.getData(), ServerNode.class);
		serverNode.setId(id);
		if(ServerWorker.instance().getServerNode().getAddress().equals(serverNode.getAddress())){
			log.info("监听到自身节点加入，无需进行连接!");
			return;
		}
		serverPeerSender.doConnectedServer(serverNode);
		log.info("新节点加入：{}",serverNode);
		ServerPeerSenderHolder.addWorker(id,serverPeerSender);
	}

	/**
	 * 路由到某个节点
	 * @param id
	 * @return
	 */
	public ServerPeerSender router(long id) {
		ServerPeerSender worker = ServerPeerSenderHolder.getWorker(id);
		return worker;
	}
}
