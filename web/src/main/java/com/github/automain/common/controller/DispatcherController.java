package com.github.automain.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.automain.bean.SysSchedule;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.dao.SysScheduleDao;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPUtil;
import com.github.fastjdbc.ConnectionPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = "/", asyncSupported = true, loadOnStartup = 2)
public class DispatcherController extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherController.class);

    private static final Map<String, BaseExecutor> REQUEST_MAPPING = new HashMap<String, BaseExecutor>();

    private static final Map<String, BaseSchedule> SCHEDULE_MAPPING = new HashMap<String, BaseSchedule>();

    static final Map<String, String> PRIVILEGE_LABEL_MAP = new HashMap<String, String>();

    static final Map<String, String> SLAVE_POOL_MAP = new HashMap<String, String>();

    @Override
    public void init() throws ServletException {
        try {
            // 初始化数据库连接池
            SystemUtil.initConnectionPool();
            // 初始化redis连接池
            RedisUtil.initJedisPool();
            // 获取数据库连接
            ConnectionPool.getConnection(null);
            // 初始化访问路径和定时任务
            initRequestMapAndSchedule();
            LOGGER.info("===============================System Start Success===============================");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            try {
                ConnectionPool.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initRequestMapAndSchedule() throws Exception {
        String controllerPath = DispatcherController.class.getResource("../../controller").getPath();
        File controllers = new File(controllerPath);
        addRequestMapping(controllers.listFiles());
        String schedulePath = DispatcherController.class.getResource("../../schedule").getPath();
        File schedules = new File(schedulePath);
        addRequestMapping(schedules.listFiles());
        boolean openSchedule = PropertiesUtil.getBooleanProperty("app.openSchedule");
        if (openSchedule) {
            Map<String, SysSchedule> map = SysScheduleDao.selectUriScheduleMap();
            addScheduleMapping(schedules.listFiles(), map);
            if (!SCHEDULE_MAPPING.isEmpty()) {
                ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(SCHEDULE_MAPPING.size());
                int now = DateUtil.getNow();
                for (BaseSchedule schedule : SCHEDULE_MAPPING.values()) {
                    int initialDelay = 0;
                    int period = schedule.getPeriod();
                    int start = schedule.getStartExecuteTime();
                    int diff = now - start;
                    if (diff > 0) {
                        if (diff > period) {
                            initialDelay = period - (diff % period);
                        } else {
                            initialDelay = period - diff;
                        }
                        scheduledThreadPool.scheduleAtFixedRate(schedule, initialDelay, period, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    private void addRequestMapping(File[] childClassList) throws Exception {
        if (childClassList != null) {
            for (File childClass : childClassList) {
                String classPath = childClass.getPath();
                if (classPath.endsWith(".class")) {
                    classPath = classPath.substring(classPath.indexOf(File.separator + "classes") + 9, classPath.lastIndexOf(".")).replace(File.separator, ".");
                    Class clazz = Class.forName(classPath);
                    if (BaseController.class.isAssignableFrom(clazz)) {
                        Method[] methods = clazz.getDeclaredMethods();
                        Object controller = clazz.getDeclaredConstructor().newInstance();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(RequestUri.class)) {
                                RequestUri requestUri = method.getAnnotation(RequestUri.class);
                                String uri = requestUri.value();
                                if (REQUEST_MAPPING.containsKey(uri)) {
                                    throw new RuntimeException("uri conflict---------->" + uri);
                                }
                                Class<?> returnType = method.getReturnType();
                                if (!JsonResponse.class.isAssignableFrom(returnType)) {
                                    throw new RuntimeException("request method return type error---------->" + method.getDeclaringClass().getName() + "#" + method.getName());
                                }
                                Parameter[] parameters = method.getParameters();
                                int size = parameters.length;
                                BaseExecutor executor = null;
                                if (size == 0) {
                                    executor = new BaseExecutor() {
                                        @Override
                                        protected JsonResponse execute(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
                                            Method method1 = controller.getClass().getMethod(method.getName());
                                            return (JsonResponse) method1.invoke(controller);
                                        }
                                    };
                                } else {
                                    Class[] parameterTypes = new Class[size];
                                    boolean existVOParam = false;
                                    for (int i = 0; i < size; i++) {
                                        Class c = parameters[i].getType();
                                        if (!HttpServletRequest.class.isAssignableFrom(c) && !Jedis.class.isAssignableFrom(c) && !HttpServletResponse.class.isAssignableFrom(c)) {
                                            if (existVOParam) {
                                                throw new RuntimeException("request method param error---------->" + method.getDeclaringClass().getName() + "#" + method.getName());
                                            }
                                            existVOParam = true;
                                        }
                                        parameterTypes[i] = c;
                                    }
                                    executor = new BaseExecutor() {
                                        @Override
                                        protected JsonResponse execute(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
                                            Object[] args = new Object[size];
                                            for (int i = 0; i < size; i++) {
                                                Class c = parameterTypes[i];
                                                if (HttpServletRequest.class.isAssignableFrom(c) || Jedis.class.isAssignableFrom(c) || HttpServletResponse.class.isAssignableFrom(c)) {
                                                    if (HttpServletRequest.class.isAssignableFrom(c)) {
                                                        args[i] = request;
                                                    } else if (Jedis.class.isAssignableFrom(c)) {
                                                        args[i] = jedis;
                                                    } else if (HttpServletResponse.class.isAssignableFrom(c)) {
                                                        args[i] = response;
                                                    }
                                                } else {
                                                    try (InputStreamReader isr = new InputStreamReader(request.getInputStream(), PropertiesUtil.DEFAULT_CHARSET);
                                                         BufferedReader br = new BufferedReader(isr)) {
                                                        StringBuilder sb = new StringBuilder();
                                                        String temp = null;
                                                        while ((temp = br.readLine()) != null) {
                                                            sb.append(temp);
                                                        }
                                                        args[i] = JSONObject.parseObject(sb.toString(), c);
                                                    } catch (Exception e) {
                                                        throw new RuntimeException("request method param error---------->" + method.getDeclaringClass().getName() + "#" + method.getName());
                                                    }
                                                }
                                            }
                                            Method method1 = controller.getClass().getMethod(method.getName(), parameterTypes);
                                            return (JsonResponse) method1.invoke(controller, args);
                                        }
                                    };
                                }
                                REQUEST_MAPPING.put(uri, executor);
                                LOGGER.info("mapping uri: " + uri);
                                String label = requestUri.label();
                                if (StringUtils.isNotBlank(label)) {
                                    PRIVILEGE_LABEL_MAP.put(uri, label);
                                }
                                String slavePoolName = requestUri.slave();
                                if (StringUtils.isNotBlank(slavePoolName)) {
                                    SLAVE_POOL_MAP.put(uri, slavePoolName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addScheduleMapping(File[] childClassList, Map<String, SysSchedule> map) throws Exception {
        if (childClassList != null && map != null) {
            for (File childClass : childClassList) {
                String classPath = childClass.getPath();
                if (classPath.endsWith(".class")) {
                    classPath = classPath.substring(classPath.indexOf(File.separator + "classes") + 9, classPath.lastIndexOf(".")).replace(File.separator, ".");
                    Class clazz = Class.forName(classPath);
                    if (BaseController.class.isAssignableFrom(clazz)) {
                        Method[] methods = clazz.getDeclaredMethods();
                        Object schedule = clazz.getDeclaredConstructor().newInstance();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(RequestUri.class)) {
                                RequestUri requestUri = method.getAnnotation(RequestUri.class);
                                String uri = requestUri.value();
                                SysSchedule s = map.get(uri);
                                if (s == null) {
                                    continue;
                                }
                                Parameter[] parameters = method.getParameters();
                                int size = parameters.length;
                                BaseSchedule executor = null;
                                if (size == 0) {
                                    executor = new BaseSchedule(uri, s.getPeriod(), s.getStartExecuteTime()) {
                                        @Override
                                        protected void execute(Jedis jedis) throws Exception {
                                            Method method1 = schedule.getClass().getMethod(method.getName());
                                            method1.invoke(schedule);
                                        }
                                    };
                                } else {
                                    Class[] parameterTypes = new Class[size];
                                    for (int i = 0; i < size; i++) {
                                        parameterTypes[i] = parameters[i].getType();
                                    }
                                    executor = new BaseSchedule(uri, s.getPeriod(), s.getStartExecuteTime()) {
                                        @Override
                                        protected void execute(Jedis jedis) throws Exception {
                                            Object[] args = new Object[size];
                                            for (int i = 0; i < size; i++) {
                                                Class c = parameterTypes[i];
                                                if (Jedis.class.isAssignableFrom(c)) {
                                                    args[i] = jedis;
                                                } else {
                                                    args[i] = null;
                                                }
                                            }
                                            Method method1 = schedule.getClass().getMethod(method.getName(), parameterTypes);
                                            method1.invoke(schedule, args);
                                        }
                                    };
                                }
                                SCHEDULE_MAPPING.put(uri, executor);
                                LOGGER.info("mapping schedule uri: " + uri);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = HTTPUtil.getRequestUri(req);
        BaseExecutor executor = REQUEST_MAPPING.get(uri);
        if (executor == null) {
            executor = new BaseExecutor() {
                @Override
                protected JsonResponse execute(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
                    return JsonResponse.getJson(404, "资源未找到");
                }
            };
        }
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(60000L);
        executor.setAsyncContext(asyncContext);
        asyncContext.start(executor);
    }

}
