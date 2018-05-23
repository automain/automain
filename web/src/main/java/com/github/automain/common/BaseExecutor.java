package com.github.automain.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.CompressUtil;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.LogUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.wapper.JspResponseWrapper;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.util.RequestUtil;
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
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class BaseExecutor extends RequestUtil implements ServiceContainer, Runnable {

    private static final String HTML_CONTENT_TYPE = "text/html; charset=" + PropertiesUtil.DEFAULT_CHARSET;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=" + PropertiesUtil.DEFAULT_CHARSET;
    private static final String ERROR_URL = "/common/error";// 错误页面
    protected static final String CODE_SUCCESS = "0";// 返回成功
    protected static final String CODE_FAIL = "1";// 返回失败
    protected static final String PAGE_BEAN_PARAM = "pageBean";// 分页对象参数名
    protected static final Logger LOGGER = LogUtil.getLoggerByName("system");

    private AsyncContext asyncContext;

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        ConnectionBean connection = null;
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        boolean toJson = "POST".equals(request.getMethod());
        boolean isDownloadRequest = false;
        try {
            jedis = RedisUtil.getJedis();
            connection = ConnectionPool.getConnectionBean(setSlavePool());
            String jspPath = getJspPath(jedis, connection, request, response);
            isDownloadRequest = "download_file".equals(jspPath);
            String content = getContent(jedis, request, response, toJson, isDownloadRequest, jspPath);
            flushResponse(request, response, isDownloadRequest, jspPath, content);
        } catch (Exception e) {
            e.printStackTrace();
            handleException(connection, request, response, toJson, isDownloadRequest, e);
        } finally {
            try {
                if (jedis != null) {
                    jedis.close();
                }
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        }
    }

    private void handleException(ConnectionBean connection, HttpServletRequest request, HttpServletResponse response, boolean toJson, boolean isDownloadRequest, Exception e) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            String msg = sw.toString();
            if (!isDownloadRequest) {
                String content = null;
                setJsonResult(request, "500", msg.substring(0, msg.indexOf(")") + 1));
                if (toJson) {
                    response.setContentType(JSON_CONTENT_TYPE);
                    try {
                        content = requestToJson(request, response, ERROR_URL);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    response.setContentType(HTML_CONTENT_TYPE);
                    try {
                        content = getJspOutput(request, response, ERROR_URL);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
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
                doRollBack();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void flushResponse(HttpServletRequest request, HttpServletResponse response, boolean isDownloadRequest, String jspPath, String content) throws IOException {
        if (!isDownloadRequest) {
            byte[] gzip = null;
            String acceptEncoding = request.getHeader("Accept-Encoding");
            if (HTML_CONTENT_TYPE.equals(response.getContentType()) && HTTPUtil.checkGzip(acceptEncoding, response, content.length(), jspPath)) {
                gzip = CompressUtil.gzipCompress(content);
            }
            if (gzip != null) {
                OutputStream os = response.getOutputStream();
                os.write(gzip);
                os.flush();
            } else {
                PrintWriter writer = response.getWriter();
                writer.write(content == null ? "" : content);
                writer.flush();
            }
        }
    }

    private String getContent(Jedis jedis, HttpServletRequest request, HttpServletResponse response, boolean toJson, boolean isDownloadRequest, String jspPath) throws ServletException, IOException {
        String content = null;
        if (toJson || jspPath == null) {
            content = requestToJson(request, response, jspPath);
            response.setContentType(JSON_CONTENT_TYPE);
        } else if (!isDownloadRequest) {
            response.setContentType(HTML_CONTENT_TYPE);
            String hasReadNotice = CookieUtil.getCookieByName(request, "hasReadNotice");
            if (hasReadNotice == null) {
                Map<String, String> noticeMap = null;
                if (jedis != null) {
                    noticeMap = jedis.hgetAll("notice_cache_key");
                } else {
                    noticeMap = (Map<String, String>) RedisUtil.LOCAL_CACHE.get("notice_cache_key");
                }
                if (noticeMap != null && !noticeMap.isEmpty()) {
                    request.setAttribute("notice_cache_key", noticeMap);
                    request.setAttribute("vEnter", "\n");
                    CookieUtil.addCookie(response, "hasReadNotice", "1", -1);
                }
            }
            content = getJspOutput(request, response, jspPath);
        }
        return content;
    }

    private String getJspPath(Jedis jedis, ConnectionBean connection, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jspPath = null;
        if (checkAuthority(jedis, request, response)) {
            setJsonResult(request, CODE_SUCCESS, "");
            jspPath = doAction(connection, jedis, request, response);
        } else {
            setJsonResult(request, "403", "无权限访问或登录已过期");
            jspPath = ERROR_URL;
        }
        return jspPath;
    }

    /**
     * 异常时回滚
     */
    protected void doRollBack() {
    }

    /**
     * 检查权限
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return checkUserAuthority(jedis, request, response);
    }

    /**
     * 公用检查用户是否登录
     *
     * @param jedis
     * @param request
     * @param response
     * @return
     */
    protected final boolean checkUserLogin(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getSessionUser(jedis, request, response) != null;
    }

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
        TbUser user = getSessionUser(jedis, request, response);
        if (user == null) {
            return false;
        }
        Set<String> roleLabel = RolePrivilegeContainer.getRoleLabelByUserId(user.getUserId());
        if (roleLabel.contains("admin")) {
            return true;
        }
        Set<String> uriSet = RolePrivilegeContainer.getRequestUrlSetByUserId(user.getUserId());
        return uriSet != null && uriSet.contains(uri);
    }

    /**
     * 定向选择从数据源连接
     *
     * @return
     */
    protected String setSlavePool() {
        return null;
    }

    /**
     * 由子类继承，返回页面字符串或json字符串
     *
     * @param connection
     * @param jedis
     * @param request
     * @param response
     * @return
     */
    protected abstract String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 将渲染后的jsp返回(可用模板技术代替)
     *
     * @param request
     * @param response
     * @param jspPath
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private static String getJspOutput(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
        JspResponseWrapper wrapper = new JspResponseWrapper(response);
        request.getRequestDispatcher("/WEB-INF/view/" + jspPath + ".jsp").include(request, wrapper);
        return wrapper.getContent();
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
     * @param response
     * @param jspPath
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private static String requestToJson(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
        JSONObject json = new JSONObject();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            Object o = request.getAttribute(key);
            if (o instanceof PageBean) {
                PageBean pageBean = (PageBean) o;
                json.put("count", pageBean.getCount());
                json.put("curr", pageBean.getCurr());
                String list = getJspOutput(request, response, jspPath);
                json.put("data", list.substring(list.indexOf("<table>") + 7, list.indexOf("</table>")));
            } else {
                json.put(key, JSON.toJSON(o));
            }
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
            accessToken = request.getHeader("Authorization");
        }
        if (accessToken != null) {
            String decrypt = EncryptUtil.AESDecrypt(accessToken.getBytes(PropertiesUtil.DEFAULT_CHARSET), PropertiesUtil.SECURITY_KEY);
            String[] arr = decrypt.split("_");
            if (arr.length == 2) {
                Long userId = Long.valueOf(arr[0]);
                String userKey = "user:" + userId;
                Long expireTime = Long.valueOf(arr[1]);
                Map<String, String> userMap = null;
                long newExpireTime = System.currentTimeMillis() + PropertiesUtil.SESSION_EXPIRE_MILLISECOND;
                long newCacheExpireTime = System.currentTimeMillis() + PropertiesUtil.CACHE_EXPIRE_MILLISECOND;
                if (jedis != null) {
                    userMap = jedis.hgetAll(userKey);
                } else {
                    Object obj = RedisUtil.LOCAL_CACHE.get(userKey);
                    if (obj != null) {
                        userMap = (Map<String, String>) obj;
                    }
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            ConnectionPool.closeConnectionBean(connection);
                        }
                    }
                } else {
                    long cacheExpire = Long.parseLong(userMap.get("expireTime"));
                    if (cacheExpire < System.currentTimeMillis()) {
                        return null;
                    } else if (expireTime < System.currentTimeMillis()) {
                        isRefresh = true;
                    }
                }
                if (isRefresh) {
                    userMap.put("expireTime", String.valueOf(newCacheExpireTime));
                    if (jedis != null) {
                        jedis.hmset(userKey, userMap);
                        jedis.expire(userKey, PropertiesUtil.CACHE_EXPIRE_SECONDS);
                    } else {
                        RedisUtil.LOCAL_CACHE.put(userKey, userMap);
                    }
                    String value = userId + "_" + newExpireTime;
                    String newAccessToken = EncryptUtil.AESEncrypt(value.getBytes(PropertiesUtil.DEFAULT_CHARSET), PropertiesUtil.SECURITY_KEY);
                    CookieUtil.addCookie(response, "accessToken", newAccessToken, -1);
                }
                TbUser user = new TbUser();
                if (userMap != null) {
                    user.setUserName(userMap.get("userName"));
                    user.setCellphone(userMap.get("cellphone"));
                    user.setEmail(userMap.get("email"));
                }
                user.setUserId(userId);
                return user;
            }
        }
        return null;
    }

}
