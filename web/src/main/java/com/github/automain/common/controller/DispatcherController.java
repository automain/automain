package com.github.automain.common.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/", asyncSupported = true, loadOnStartup = 2)
public class DispatcherController extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherController.class);

    private static final Map<String, BaseExecutor> REQUEST_MAPPING = new HashMap<String, BaseExecutor>();

    static final Map<String, String> PRIVILEGE_LABEL_MAP = new HashMap<String, String>();

    static final Map<String, String> SLAVE_POOL_MAP = new HashMap<String, String>();

    @Override
    public void init() throws ServletException {
        Connection connection = null;
        try {
            // 初始化数据库连接池
            SystemUtil.initConnectionPool();
            // 初始化redis连接池
            RedisUtil.initJedisPool();
            // 初始化访问路径
            REQUEST_MAPPING.clear();
            REQUEST_MAPPING.putAll(initRequestMap());
            // 获取数据库连接
            connection = ConnectionPool.getConnection(null);
            // 初始化定时任务
//            SystemUtil.reloadSchedule(connection);
            LOGGER.info("===============================System Start Success===============================");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            try {
                ConnectionPool.close(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, BaseExecutor> initRequestMap() throws Exception {
        String controllerPath = DispatcherController.class.getResource("../../controller").getPath();
        File file = new File(controllerPath);
        File[] childClassList = file.listFiles();
        Map<String, BaseExecutor> requestMap = new HashMap<String, BaseExecutor>();
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
                                if (requestMap.containsKey(uri)) {
                                    throw new RuntimeException("uri conflict---------->" + uri);
                                }
                                Parameter[] parameters = method.getParameters();
                                if (parameters.length != 4 || !Connection.class.isAssignableFrom(parameters[0].getType())
                                        || !Jedis.class.isAssignableFrom(parameters[1].getType())
                                        || !HttpServletRequest.class.isAssignableFrom(parameters[2].getType())
                                        || !HttpServletResponse.class.isAssignableFrom(parameters[3].getType())) {
                                    throw new RuntimeException("request method param error---------->" + method.getDeclaringClass().getName() + "#" + method.getName());
                                }
                                Class<?> returnType = method.getReturnType();
                                if (!JsonResponse.class.isAssignableFrom(returnType)) {
                                    throw new RuntimeException("request method return type error---------->" + method.getDeclaringClass().getName() + "#" + method.getName());
                                }
                                BaseExecutor executor = new BaseExecutor() {
                                    @Override
                                    protected JsonResponse execute(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
                                        Method method1 = controller.getClass().getMethod(method.getName(), Connection.class, Jedis.class, HttpServletRequest.class, HttpServletResponse.class);
                                        return (JsonResponse) method1.invoke(controller, connection, jedis, request, response);
                                    }
                                };
                                requestMap.put(uri, executor);
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
        return requestMap;
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
            executor = new ResourceNotFoundExecutor();
        }
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(60000L);
        executor.setAsyncContext(asyncContext);
        asyncContext.start(executor);
    }

}
