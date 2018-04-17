package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestMappingContainer;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCacheExecutor extends BaseExecutor {

    private static List<String> INNER_IP_PORTS = null;

    private static List<String> INNER_IPS = null;

    static {
        String innerIpPort = PropertiesUtil.CONFIG_PROPERTIES.getProperty("innerIpPort");
        if (StringUtils.isNotBlank(innerIpPort)) {
            INNER_IP_PORTS = Arrays.asList(innerIpPort.split(","));
            INNER_IPS = new ArrayList<String>();
            for (String ipPort : INNER_IP_PORTS) {
                INNER_IPS.add(ipPort.split(":")[0]);
            }
        }
    }

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (INNER_IPS != null && INNER_IPS.contains(HTTPUtil.getRequestIp(request))) || super.checkAuthority(jedis, request, response);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String reloadLabel = getString("reloadLabel", request);
        String logReloadLabel = null;
        if (reloadLabel != null) {
            switch (reloadLabel) {
                case "requestMapping":
                    RequestMappingContainer.reloadRequestMapping();
                    break;
                case "properties":
                    PropertiesUtil.reloadProperties();
                    logReloadLabel = "配置文件";
                    break;
                case "dictionary":
                    DictionaryContainer.reloadDictionary();
                    logReloadLabel = "数据字典";
                    break;
                case "role":
                    RolePrivilegeContainer.reloadRolePrivilege();
                    logReloadLabel = "角色权限";
                    break;
                default:
                    RequestMappingContainer.reloadRequestMapping();
                    logReloadLabel = "访问路径";
                    break;
            }
        } else {
            RequestMappingContainer.reloadRequestMapping();
        }
        String thisIpPort = request.getServerName() + ":" + request.getServerPort();
        LOGGER.info("刷新缓存地址:" + thisIpPort + ",刷新缓存项:" + logReloadLabel);
        if (INNER_IPS != null && !INNER_IPS.contains(HTTPUtil.getRequestIp(request))) {
            String requestUri = request.getRequestURI();
            for (String ipPort : INNER_IP_PORTS) {
                if (thisIpPort.equals(ipPort)) {
                    continue;
                }
                HTTPUtil.httpPost("http://" + ipPort + requestUri);
            }
        }
        setJsonResult(request, CODE_SUCCESS, "刷新成功");
        return null;
    }
}
