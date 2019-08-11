package com.github.automain.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbUser;
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
import com.github.fastjdbc.util.RequestUtil;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class BaseExecutor extends RequestUtil implements ServiceContainer, Runnable {

    protected static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    protected static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 300;
    protected static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");
    protected static final String CODE_SUCCESS = "0";// 返回成功
    protected static final String CODE_FAIL = "1";// 返回失败
    protected static final String PAGE_BEAN_PARAM = "pageBean";// 分页对象参数名
    protected static final Logger LOGGER = SystemUtil.getLogger();
    private static final List<String> WHITE_LIST_URL = List.of("/test");

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
            if (checkUserAuthority(jedis, request, response)) {
                setJsonResult(request, CODE_SUCCESS, "");
                execute(connection, jedis, request, response);
            } else {
                setJsonResult(request, "403", "无权限访问或登录已过期");
            }
            if (!HTTPUtil.FILE_CONTENT_TYPE.equals(response.getContentType())) {
                response.setContentType(HTTPUtil.JSON_CONTENT_TYPE);
                String content = requestToJson(request);
                if (HTTPUtil.HTML_CONTENT_TYPE.equals(response.getContentType()) && HTTPUtil.checkGzip(request, response, content.length())) {
                    byte[] gzip = CompressUtil.gzipCompress(content);
                    OutputStream os = response.getOutputStream();
                    os.write(gzip);
                    os.flush();
                } else {
                    PrintWriter writer = response.getWriter();
                    writer.write(content == null ? "" : content);
                    writer.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw)) {
                e.printStackTrace(pw);
                String msg = sw.toString();
                if (!HTTPUtil.FILE_CONTENT_TYPE.equals(response.getContentType())) {
                    String content = null;
                    setJsonResult(request, "500", msg);
                    response.setContentType(HTTPUtil.JSON_CONTENT_TYPE);
                    try {
                        content = requestToJson(request);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    PrintWriter writer = response.getWriter();
                    writer.write(content == null ? "" : content);
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
    protected abstract void execute(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception;

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
     * 设置json返回状态码和状态信息
     *
     * @param request
     * @param code
     * @param msg
     */
    protected static void setJsonResult(HttpServletRequest request, String code, String msg) {
        request.setAttribute("code", code);
        request.setAttribute("msg", msg);
    }

    /**
     * request中参数封装到json
     *
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private static String requestToJson(HttpServletRequest request) throws ServletException, IOException {
        JSONObject json = new JSONObject();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            Object o = request.getAttribute(key);
            json.put(key, JSON.toJSON(o));
        }
        return json.toJSONString();
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
                int newExpireTime = DateUtil.getNowSecond() + SESSION_EXPIRE_SECONDS;
                int newCacheExpireTime = DateUtil.getNowSecond() + CACHE_EXPIRE_SECONDS;
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
                            TbUser user = TB_USER_SERVICE.selectTableById(connection, userId);
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
