package com.github.automain.common.controller;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.container.DictionaryContainer;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.view.ResourceNotFoundExecutor;
import com.github.automain.schedule.ScheduleThread;
import com.github.automain.user.view.LoginExecutor;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/", asyncSupported = true, loadOnStartup = 2)
public class DispatcherController extends HttpServlet {

    private static ServletContext SERVLET_CONTEXT;

    private static Map<String, BaseExecutor> REQUEST_MAPPING;

    private static ScheduledExecutorService SCHEDULE_THREAD_POOL = null;

    @Override
    public void init() throws ServletException {
        ConnectionBean connection = null;
        Jedis jedis = null;
        try {
            // 初始化context
            SERVLET_CONTEXT = getServletContext();
            // 初始化数据库连接池
            SystemUtil.initConnectionPool();
            // 初始化redis连接池
            SystemUtil.initJedisPool();
            // 初始化日志
            SystemUtil.initLogConfig();
            // 初始化访问路径
            REQUEST_MAPPING = initRequestMap(DispatcherController.class.getResource("/").getPath().replace("test-classes", "classes"), new HashMap<String, BaseExecutor>());
            // 获取数据库连接
            connection = ConnectionPool.getConnectionBean(null);
            // 获取redis连接
            jedis = RedisUtil.getJedis();
            // 初始化字典表缓存
            DictionaryContainer.reloadDictionary(jedis, connection);
            // 初始化人员角色权限缓存
            RolePrivilegeContainer.reloadRolePrivilege(jedis, connection, getRequestUrlList());
            // 初始化定时任务
            reloadSchedule(connection, jedis);
            System.err.println("===============================Init Success===============================");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            SystemUtil.closeJedisAndConnectionBean(jedis, connection);
        }
    }

    public static void setServletContext(String key, String value) {
        SERVLET_CONTEXT.setAttribute(key, value);
    }

    @SuppressWarnings("unchecked")
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

    public static void reloadSchedule(ConnectionBean connection, Jedis jedis) throws SQLException {
        if (PropertiesUtil.OPEN_SCHEDULE) {
            if (SCHEDULE_THREAD_POOL != null) {
                SCHEDULE_THREAD_POOL.shutdown();
                SCHEDULE_THREAD_POOL = null;
            }
            TbSchedule bean = new TbSchedule();
            bean.setIsDelete(0);
            List<TbSchedule> scheduleList = ServiceContainer.TB_SCHEDULE_SERVICE.selectTableByBean(connection, bean);
            int size = scheduleList.size();
            if (size > 0) {
                SCHEDULE_THREAD_POOL = Executors.newScheduledThreadPool(size);
                for (TbSchedule schedule : scheduleList) {
                    startSchedule(schedule, jedis);
                }
            }
        }
    }

    private static void startSchedule(TbSchedule schedule, Jedis jedis) {
        long initialDelay = 0L;
        long jump = schedule.getDelayTime();
        long now = SystemUtil.getNowSecond();
        long start = schedule.getStartExecuteTime().getTime() / 1000;
        long diff = now - start;
        if (diff > 0) {
            if (diff > jump) {
                initialDelay = jump - (diff % jump);
            } else {
                initialDelay = jump - diff;
            }
            SCHEDULE_THREAD_POOL.scheduleAtFixedRate(new ScheduleThread(schedule.getScheduleUrl(), jedis, jump), initialDelay, jump, TimeUnit.SECONDS);
        }
    }

    public static List<String> getRequestUrlList() {
        return new ArrayList<String>(REQUEST_MAPPING.keySet());
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
