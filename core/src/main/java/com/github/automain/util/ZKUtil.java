package com.github.automain.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZKUtil {

    private static RetryPolicy RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

    /**
     * 获取zookeeper连接
     *
     * @param namespace
     * @return
     */
    public static CuratorFramework getClient(String namespace) {
        if (!PropertiesUtil.OPEN_ZOOKEEPER) {
            return null;
        }
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(PropertiesUtil.ZK_SERVERS)
                .retryPolicy(RETRY_POLICY)
                .namespace(namespace == null ? PropertiesUtil.DETAULT_NAMESPACE : namespace)
                .build();
        client.start();
        return client;
    }

    /**
     * 消息订阅
     *
     * @param client
     * @param path
     * @param listener
     * @throws Exception
     */
    public static void addListenerByPath(CuratorFramework client, String path, NodeCacheListener listener) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().forPath(path);
        }
        NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(listener);
        cache.start();
    }

}
