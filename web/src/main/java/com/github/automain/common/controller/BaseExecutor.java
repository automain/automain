package com.github.automain.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.automain.bean.SysUser;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
import com.github.fastjdbc.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public abstract class BaseExecutor implements Runnable {

    private static final List<String> WHITE_LIST_URL = List.of("/getCaptcha", "/login", "/upload");

    private AsyncContext asyncContext;

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        Connection connection = null;
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        try {
            jedis = RedisUtil.getJedis();
            String uri = HTTPUtil.getRequestUri(request);
            connection = ConnectionPool.getConnection(DispatcherController.SLAVE_POOL_MAP.get(uri));
            JsonResponse jsonResponse = null;
            if (checkUserAuthority(connection, jedis, request, response)) {
                jsonResponse = execute(connection, jedis, request, response);
            } else {
                jsonResponse = JsonResponse.getJson(403, "无权限访问或登录已过期");
            }
            if (response.getContentType() == null) {
                response.setContentType(HTTPUtil.JSON_CONTENT_TYPE);
                byte[] content = JSON.toJSONBytes(jsonResponse, SerializerFeature.WriteMapNullValue);
                if (HTTPUtil.checkGzip(request, response, content.length)) {
                    OutputStream os = response.getOutputStream();
                    os.write(CompressUtil.gzipCompress(content));
                    os.flush();
                } else {
                    PrintWriter writer = response.getWriter();
                    writer.write(new String(content, PropertiesUtil.DEFAULT_CHARSET));
                    writer.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw)) {
                e.printStackTrace(pw);
                String message = sw.toString();
                if (response.getContentType() == null) {
                    JsonResponse jsonResponse = JsonResponse.getJson(500, message);
                    String content = JSON.toJSONString(jsonResponse, SerializerFeature.WriteMapNullValue);
                    response.setContentType(HTTPUtil.JSON_CONTENT_TYPE);
                    PrintWriter writer = response.getWriter();
                    writer.write(content);
                    writer.flush();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    ConnectionPool.rollback(connection);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            SystemUtil.closeJedisAndConnection(jedis, connection);
            asyncContext.complete();
        }
    }

    /**
     * 由子类继承，处理请求
     *
     * @param connection
     * @param jedis
     * @param request
     * @param response
     * @return
     */
    protected abstract JsonResponse execute(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 公用检查用户权限
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkUserAuthority(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = HTTPUtil.getRequestUri(request);
        if (WHITE_LIST_URL.contains(uri)) {
            return true;
        }
        SysUser user = BaseController.getSessionUser(connection, jedis, request, response);
        if (user == null) {
            return false;
        }
        String label = DispatcherController.PRIVILEGE_LABEL_MAP.get(uri);
        if (label == null) {
            return true;
        }
        Set<String> privilegeSet = null;
        String userPrivilegeKey = "userPrivilege:" + user.getGid();
        if (jedis != null) {
            privilegeSet = jedis.smembers(userPrivilegeKey);
        } else {
            privilegeSet = RedisUtil.getLocalCache(userPrivilegeKey);
        }
        if (privilegeSet == null) {
            return false;
        }
        return privilegeSet.contains("admin") || privilegeSet.contains(label);
    }

}
