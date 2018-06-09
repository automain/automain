package com.github.automain.common.controller;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbConfig;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.view.ResourceNotFoundExecutor;
import com.github.automain.user.view.LoginExecutor;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import redis.clients.jedis.Jedis;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/", asyncSupported = true, loadOnStartup = 2)
public class DispatcherController extends HttpServlet {

    private static ServletContext SERVLET_CONTEXT;

    private static Map<String, BaseExecutor> REQUEST_MAPPING;

    private static List<String> INNER_IP_LIST;

    private static List<String> INNER_IP_PORT_LIST;

    @Override
    public void init() throws ServletException {
        ConnectionBean connection = null;
        Jedis jedis = null;
        try {
            // 初始化context
            SERVLET_CONTEXT = getServletContext();
            // 初始化数据库连接池
            SystemUtil.initConnectionPool();
            connection = ConnectionPool.getConnectionBean(null);
            // 初始化静态资源版本
            reloadStaticVersion(connection);
            // 初始化redis连接池
            SystemUtil.initJedisPool();
            jedis = RedisUtil.getJedis();
            // 初始化访问路径
            REQUEST_MAPPING = initRequestMap(DispatcherController.class.getResource("/").getPath().replace("test-classes", "classes"), new HashMap<String, BaseExecutor>());
            // 初始化字典表缓存
            DictionaryContainer.reloadDictionary(jedis, connection);
            // 初始化人员角色权限缓存
            RolePrivilegeContainer.reloadRolePrivilege(jedis, connection, getRequestUrlList());
            // 初始化内部地址端口
            reloadInnerIpPort(connection);
            System.err.println("===============================Init Success===============================");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            SystemUtil.closeJedisAndConnectionBean(jedis, connection);
        }
    }

    public static void reloadStaticVersion(ConnectionBean connection) throws SQLException {
        TbConfig bean = new TbConfig();
        bean.setConfigKey("staticVersion");
        bean.setIsDelete(0);
        TbConfig config = ServiceContainer.TB_CONFIG_SERVICE.selectOneTableByBean(connection, bean);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (config != null) {
            String staticVersion = config.getConfigValue();
            long sv = Long.parseLong(staticVersion);
            config.setConfigValue(String.valueOf(sv + 1));
            config.setUpdateTime(now);
            SERVLET_CONTEXT.setAttribute("staticVersion", config.getConfigValue());
            ServiceContainer.TB_CONFIG_SERVICE.updateTable(connection, config, false);
        } else {
            SERVLET_CONTEXT.setAttribute("staticVersion", "0");
            bean.setConfigValue("0");
            bean.setConfigComment("静态资源版本");
            bean.setCreateTime(now);
            bean.setUpdateTime(now);
            ServiceContainer.TB_CONFIG_SERVICE.insertIntoTable(connection, bean);
        }
    }

    public static void reloadInnerIpPort(ConnectionBean connection) throws SQLException {
        INNER_IP_LIST = ServiceContainer.TB_INNER_IP_PORT_SERVICE.selectInnerIpList(connection);
        INNER_IP_PORT_LIST = ServiceContainer.TB_INNER_IP_PORT_SERVICE.selectInnerIpPortList(connection);
    }

    private Map<String, BaseExecutor> initRequestMap(String basePath, Map<String, BaseExecutor> requestMap) throws Exception {
        File file = new File(basePath);
        File[] childClassList = file.listFiles();
        if (childClassList != null) {
            for (File childClass : childClassList) {
                if (childClass.isDirectory()) {
                    initRequestMap(childClass.getPath(), requestMap);
                } else {
                    String classPath = childClass.getPath();
                    if (classPath.endsWith(".class")) {
                        classPath = classPath.substring(classPath.indexOf("\\classes") + 9, classPath.lastIndexOf("."));
                        classPath = classPath.replace("\\", ".");
                        if (classPath.endsWith("Executor")) {
                            Class clazz = Class.forName(classPath);
                            if (BaseExecutor.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(RequestUrl.class)) {
                                RequestUrl annotation = (RequestUrl) clazz.getAnnotation(RequestUrl.class);
                                String url = annotation.value();
                                if (requestMap.containsKey(url)) {
                                    throw new RuntimeException("url conflict:\nurl---------->"
                                            + url + "\nconflict Class1---------->"
                                            + requestMap.get(url).getClass().getName()
                                            + "\nconflict Class2---------->" + clazz.getName());
                                }
                                requestMap.put(url, (BaseExecutor) clazz.newInstance());
                                System.out.println(url + "\t\t\t\t\t" + clazz.getName());
                            }
                        }
                    }
                }
            }
        }
        return requestMap;
    }

    public static List<String> getRequestUrlList() {
        return new ArrayList<String>(REQUEST_MAPPING.keySet());
    }

    public static List<String> getInnerIpList() {
        return INNER_IP_LIST;
    }

    public static List<String> getInnerIpPortList() {
        return INNER_IP_PORT_LIST;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BaseExecutor executor = null;
        String uri = HTTPUtil.getRequestUri(req);
        try {
            if ("/".equals(uri)) {
                executor = new LoginExecutor();
            } else {
                executor = REQUEST_MAPPING.get(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (executor == null) {
                executor = new ResourceNotFoundExecutor();
            }
            final AsyncContext asyncContext = req.startAsync(req, resp);
            asyncContext.setTimeout(100000L);
            executor.setAsyncContext(asyncContext);
            asyncContext.start(executor);
        }
    }

}
