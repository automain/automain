package com.github.automain.common;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMappingContainer {

    private static Map<String, Class<?>> REQUEST_MAPPING = null;

    static {
        reloadRequestMapping();
    }

    public static void reloadRequestMapping() {
        ConcurrentHashMap<String, Class<?>> result = new ConcurrentHashMap<String, Class<?>>();
        ConnectionBean connection = null;
        try {
            connection = ConnectionPool.getConnectionBean(null);
            List<TbRequestMapping> requestMappingList = ServiceContainer.TB_REQUEST_MAPPING_SERVICE.selectAllTable(connection);
            for (TbRequestMapping requestMapping : requestMappingList) {
                Class clazz = Class.forName(requestMapping.getOperationClass());
                if (BaseExecutor.class.isAssignableFrom(clazz)) {
                    String requestUrl = requestMapping.getRequestUrl();
                    result.put(requestUrl, clazz);
                }
            }
            REQUEST_MAPPING = result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Class<?> getRequestMapping(String uri) {
        return REQUEST_MAPPING != null ? REQUEST_MAPPING.get(uri) : null;
    }
}
