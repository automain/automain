package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.controller.DispatcherController;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.util.http.HTTPUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequestUrl("/reload/cache")
public class ReloadCacheExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (DispatcherController.getInnerIpList().contains(HTTPUtil.getRequestIp(request))) || super.checkAuthority(jedis, request, response);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reloadLabel = getString("reloadLabel", request);
        String logReloadLabel = null;
        if (reloadLabel != null) {
            switch (reloadLabel) {
                case "properties":
                    PropertiesUtil.reloadProperties();
                    logReloadLabel = "配置文件";
                    break;
                case "dictionary":
                    DictionaryContainer.reloadDictionary(jedis, connection);
                    logReloadLabel = "数据字典";
                    break;
                case "role":
                    RolePrivilegeContainer.reloadRolePrivilege(jedis, connection, DispatcherController.getRequestUrlList());
                    logReloadLabel = "角色权限";
                    break;
                case "staticVersion":
                    DispatcherController.reloadStaticVersion(connection);
                    logReloadLabel = "静态资源";
                    break;
                case "ipport":
                    DispatcherController.reloadInnerIpPort(connection);
                    logReloadLabel = "地址端口";
                    break;
            }
        }
        if ("properties".equals(reloadLabel)) {
            String thisIpPort = request.getServerName() + ":" + request.getServerPort();
            LOGGER.info("刷新缓存地址:" + thisIpPort + ",刷新缓存项:" + logReloadLabel);
            if (!DispatcherController.getInnerIpList().contains(HTTPUtil.getRequestIp(request))) {
                String requestUri = request.getRequestURI();
                for (String ipPort : DispatcherController.getInnerIpPortList()) {
                    if (thisIpPort.equals(ipPort)) {
                        continue;
                    }
                    HTTPUtil.sendRequest("http://" + ipPort + requestUri, HTTPUtil.POST_METHOD, false);
                }
            }
        }
        setJsonResult(request, CODE_SUCCESS, "刷新成功");
        return null;
    }
}
