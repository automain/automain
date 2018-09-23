package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbConfig;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.controller.DispatcherController;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.ZKUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import org.apache.curator.framework.CuratorFramework;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.Timestamp;

@RequestUrl("/reload/cache")
public class ReloadCacheExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reloadLabel = getString("reloadLabel", request);
        if (reloadLabel != null) {
            switch (reloadLabel) {
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
                    refreshStaticVersion();
                    try (CuratorFramework client = ZKUtil.getClient(null)) {
                        if (client != null) {
                            client.setData().forPath("/staticVersion");
                        } else {
                            SystemUtil.reloadStaticVersion(request.getServletContext(), connection);
                        }
                    }
                    break;
            }
        }
        setJsonResult(request, CODE_SUCCESS, "刷新成功");
        return null;
    }

    private void refreshStaticVersion() throws SQLException {
        ConnectionBean connection = null;
        try {
            connection = ConnectionPool.getConnectionBean(null);
            TbConfig bean = new TbConfig();
            bean.setConfigKey("staticVersion");
            TbConfig config = ServiceContainer.TB_CONFIG_SERVICE.selectOneTableByBean(connection, bean);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (config != null) {
                String staticVersion = config.getConfigValue();
                long sv = Long.parseLong(staticVersion);
                config.setConfigValue(String.valueOf(sv + 1));
                config.setUpdateTime(now);
                TB_CONFIG_SERVICE.updateTable(connection, config, false);
            } else {
                bean.setConfigValue("1");
                bean.setConfigComment("静态资源版本");
                bean.setCreateTime(now);
                bean.setUpdateTime(now);
                bean.setIsDelete(0);
                ServiceContainer.TB_CONFIG_SERVICE.insertIntoTable(connection, bean);
            }
        } finally {
            ConnectionPool.closeConnectionBean(connection);
        }
    }
}
