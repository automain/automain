package com.github.automain.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.automain.bean.SysUser;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseExecutor implements Runnable {

    private static final List<String> WHITE_LIST_URL = List.of("/getCaptcha", "login");
    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 600;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");
    private static final String AUTHORIZATION = "Authorization";

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
        SysUser user = getSessionUser(connection, jedis, request, response);
        if (user == null) {
            return false;
        }
        Integer userId = user.getId();
        Set<String> privilegeSet = null;
        String userPrivilegeKey = "userPrivilege:" + userId;
        if (jedis != null) {
            privilegeSet = jedis.smembers(userPrivilegeKey);
        } else {
            privilegeSet = RedisUtil.getLocalCache(userPrivilegeKey);
        }
        if (privilegeSet == null) {
            return false;
        }
        if (privilegeSet.contains("admin")) {
            return true;
        }
        String label = DispatcherController.PRIVILEGE_LABEL_MAP.get(uri);
        return label == null || privilegeSet.contains(label);
    }

    /**
     * 从session中获取当前登录用户
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     */
    protected static SysUser getSessionUser(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null) {
            String decrypt = EncryptUtil.AESDecrypt(authorization.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
            String[] arr = decrypt.split("_");
            if (arr.length == 2) {
                Integer userId = Integer.valueOf(arr[0]);
                String userCacheKey = "user:" + userId;
                String userPrivilegeKey = "userPrivilege:" + userId;
                int expireTime = Integer.parseInt(arr[1]);
                Map<String, String> userCacheMap = null;
                int now = DateUtil.getNow();
                int newExpireTime = now + SESSION_EXPIRE_SECONDS;
                int newCacheExpireTime = now + CACHE_EXPIRE_SECONDS;
                if (jedis != null) {
                    userCacheMap = jedis.hgetAll(userCacheKey);
                } else {
                    userCacheMap = RedisUtil.getLocalCache(userCacheKey);
                }
                boolean isRefresh = false;
                if (userCacheMap == null) {
                    if (expireTime < now) {
                        return null;
                    } else {
                        SysUser user = ServiceDaoContainer.SYS_USER_DAO.selectTableById(connection, new SysUser().setId(userId));
                        userCacheMap = new HashMap<String, String>();
                        userCacheMap.put("userName", user.getUserName());
                        userCacheMap.put("phone", user.getPhone());
                        userCacheMap.put("email", user.getEmail());
                        userCacheMap.put("gid", user.getGid());
                        isRefresh = true;
                    }
                } else {
                    int cacheExpire = Integer.parseInt(userCacheMap.get("expireTime"));
                    if (cacheExpire < now) {
                        RedisUtil.delLocalCache(userCacheKey);
                        return null;
                    } else if (expireTime < now) {
                        isRefresh = true;
                    }
                }
                if (isRefresh) {
                    userCacheMap.put("expireTime", String.valueOf(newCacheExpireTime));
                    Set<String> privilegeSet = ServiceDaoContainer.SYS_PRIVILEGE_DAO.selectUserPrivilege(connection, userId);
                    if (jedis != null) {
                        jedis.del(userCacheKey);
                        jedis.hmset(userCacheKey, userCacheMap);
                        jedis.expire(userCacheKey, CACHE_EXPIRE_SECONDS);
                        String[] privilegeArr = new String[privilegeSet.size()];
                        privilegeArr = privilegeSet.toArray(privilegeArr);
                        jedis.del(userPrivilegeKey);
                        jedis.sadd(userPrivilegeKey, privilegeArr);
                        jedis.expire(userPrivilegeKey, CACHE_EXPIRE_SECONDS);
                    } else {
                        RedisUtil.delLocalCache(userCacheKey);
                        RedisUtil.setLocalCache(userCacheKey, userCacheMap);
                        RedisUtil.delLocalCache(userPrivilegeKey);
                        RedisUtil.setLocalCache(userPrivilegeKey, privilegeSet);
                    }
                    String value = userId + "_" + newExpireTime;
                    authorization = EncryptUtil.AESEncrypt(value.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
                    response.setHeader(AUTHORIZATION, authorization);
                }
                return new SysUser().setId(userId).setUserName(userCacheMap.get("userName")).setPhone(userCacheMap.get("phone")).setEmail(userCacheMap.get("email")).setGid(userCacheMap.get("gid"));
            }
        }
        return null;
    }

}
