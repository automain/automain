package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.controller.DispatcherController;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/reload/cache")
public class ReloadCacheExecutor extends BaseExecutor {

    static {
        try {
            CuratorFramework client = SystemUtil.getClient(null);
            if (client != null) {
                addListenerByLabel(client, "properties");
                addListenerByLabel(client, "dictionary");
                addListenerByLabel(client, "role");
                addListenerByLabel(client, "staticVersion");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addListenerByLabel(CuratorFramework client, String label) throws Exception {
        String path = "/" + label;
        if (client.checkExists().forPath(path) == null) {
            client.create().creatingParentsIfNeeded().forPath(path);
        }
        NodeCache cache = new NodeCache(client, path);
        NodeCacheListener listener = () -> {
            LOGGER.info("receive notify to reload data, reload label is " + label + ", reload time is " + DateUtil.getNow(DateUtil.SIMPLE_DATE_TIME_PATTERN));
            reloadByLabel(label);
        };
        cache.getListenable().addListener(listener);
        cache.start();
    }

    private static void reloadByLabel(String label) throws Exception {
        Jedis jedis = null;
        ConnectionBean connection = null;
        try {
            jedis = RedisUtil.getJedis();
            connection = ConnectionPool.getConnectionBean(null);
            switch (label) {
                case "properties": //配置文件
                    PropertiesUtil.reloadProperties();
                    break;
                case "dictionary": // 数据字典
                    DictionaryContainer.reloadDictionary(jedis, connection);
                    break;
                case "role": // 角色权限
                    RolePrivilegeContainer.reloadRolePrivilege(jedis, connection, DispatcherController.getRequestUrlList());
                    break;
                case "staticVersion": // 静态资源
                    DispatcherController.reloadStaticVersion(connection);
                    break;
            }
        } finally {
            SystemUtil.closeJedisAndConnectionBean(jedis, connection);
        }

    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reloadLabel = getString("reloadLabel", request);
        if (reloadLabel != null) {
            try (CuratorFramework client = SystemUtil.getClient(null)) {
                if (client != null) {
                    client.setData().forPath("/" + reloadLabel);
                } else {
                    reloadByLabel(reloadLabel);
                }
            }
        }
        setJsonResult(request, CODE_SUCCESS, "刷新成功");
        return null;
    }
}
