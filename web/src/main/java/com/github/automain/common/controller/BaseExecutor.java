package com.github.automain.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.automain.bean.TbUser;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseExecutor extends BaseController implements Runnable {

    private static final List<String> WHITE_LIST_URL = List.of("/test");
    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 300;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");

    private AsyncContext asyncContext;

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        ConnectionBean connection = null;
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        try {
            jedis = RedisUtil.getJedis();
            String uri = HTTPUtil.getRequestUri(request);
            connection = ConnectionPool.getConnectionBean(DispatcherController.SLAVE_POOL_MAP.get(uri));
            JsonResponse jsonResponse = null;
            if (checkUserAuthority(jedis, request, response)) {
                jsonResponse = execute(connection, jedis, request, response);
            } else {
                jsonResponse = JsonResponse.getJson(403, "无权限访问或登录已过期");
            }
            if (!HTTPUtil.FILE_CONTENT_TYPE.equals(response.getContentType())) {
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
                if (!HTTPUtil.FILE_CONTENT_TYPE.equals(response.getContentType())) {
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
                    ConnectionPool.rollbackConnectionBean(connection);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            SystemUtil.closeJedisAndConnectionBean(jedis, connection);
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
    protected abstract JsonResponse execute(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 公用检查用户权限
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    private boolean checkUserAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = HTTPUtil.getRequestUri(request);
        if (WHITE_LIST_URL.contains(uri)) {
            return true;
        }
        TbUser user = getSessionUser(jedis, request, response);
        if (user == null) {
            return false;
        }
        Set<String> roleLabel = RolePrivilegeContainer.getRoleLabelByUserId(jedis, user.getUserId());
        if (roleLabel == null) {
            return false;
        }
        if (roleLabel.contains("admin")) {
            return true;
        }
        Set<String> labels = DispatcherController.PRIVILEGE_LABEL_MAP.get(uri);
        if (CollectionUtils.isEmpty(labels)) {
            return true;
        }
        Set<String> privileges = RolePrivilegeContainer.getPrivilegeSetByUserId(jedis, user.getUserId());
        if (privileges != null) {
            for (String label : labels) {
                if (privileges.contains(label)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从session中获取当前登录用户
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     */
    protected static TbUser getSessionUser(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessToken = CookieUtil.getCookieByName(request, "accessToken");
        if (accessToken == null) {
            accessToken = request.getHeader("accessToken");
        }
        if (accessToken != null) {
            HTTPUtil.setResponseHeader(response, "accessToken", accessToken);
            String decrypt = EncryptUtil.AESDecrypt(accessToken.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
            String[] arr = decrypt.split("_");
            if (arr.length == 2) {
                Long userId = Long.valueOf(arr[0]);
                String userKey = "user:" + userId;
                Long expireTime = Long.valueOf(arr[1]);
                Map<String, String> userMap = null;
                int newExpireTime = DateUtil.getNow() + SESSION_EXPIRE_SECONDS;
                int newCacheExpireTime = DateUtil.getNow() + CACHE_EXPIRE_SECONDS;
                if (jedis != null) {
                    userMap = jedis.hgetAll(userKey);
                } else {
                    userMap = RedisUtil.getLocalCache(userKey);
                }
                boolean isRefresh = false;
                if (userMap == null) {
                    if (expireTime < System.currentTimeMillis()) {
                        return null;
                    } else {
                        ConnectionBean connection = null;
                        try {
                            connection = ConnectionPool.getConnectionBean(null);
                            TbUser user = ServiceContainer.TB_USER_SERVICE.selectTableById(connection, userId);
                            userMap = new HashMap<String, String>();
                            userMap.put("userName", user.getUserName());
                            userMap.put("cellphone", user.getCellphone());
                            userMap.put("email", user.getEmail());
                            isRefresh = true;
                        } finally {
                            ConnectionPool.closeConnectionBean(connection);
                        }
                    }
                } else {
                    long cacheExpire = Long.parseLong(userMap.get("expireTime"));
                    if (cacheExpire < System.currentTimeMillis()) {
                        RedisUtil.delLocalCache(userKey);
                        return null;
                    } else if (expireTime < System.currentTimeMillis()) {
                        isRefresh = true;
                    }
                }
                if (isRefresh) {
                    userMap.put("expireTime", String.valueOf(newCacheExpireTime));
                    if (jedis != null) {
                        jedis.hmset(userKey, userMap);
                        jedis.expire(userKey, CACHE_EXPIRE_SECONDS);
                    } else {
                        RedisUtil.setLocalCache(userKey, userMap);
                    }
                    String value = userId + "_" + newExpireTime;
                    String newAccessToken = EncryptUtil.AESEncrypt(value.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
                    CookieUtil.addCookie(response, "accessToken", newAccessToken, -1);
                    HTTPUtil.setResponseHeader(response, "accessToken", newAccessToken);
                }
                TbUser user = new TbUser();
                user.setUserName(userMap.get("userName"));
                user.setCellphone(userMap.get("cellphone"));
                user.setEmail(userMap.get("email"));
                user.setUserId(userId);
                return user;
            }
        }
        return null;
    }

}
