package com.github.automain.common;

import com.github.automain.common.bean.TbConfig;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.view.ResourceNotFoundExecutor;
import com.github.automain.user.view.LoginExecutor;
import com.github.automain.util.HTTPUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@WebServlet(urlPatterns = "/", asyncSupported = true, loadOnStartup = 2)
public class DispatcherController extends HttpServlet {

    private static ServletContext SERVLET_CONTEXT;

    @Override
    public void init() throws ServletException {
        initConnectionPool();
        initStaticVersion();
    }

    private void initStaticVersion() {
        SERVLET_CONTEXT = getServletContext();
        ConnectionBean connection = null;
        try {
            connection = ConnectionPool.getConnectionBean(null);
            TbConfig bean = new TbConfig();
            bean.setConfigKey("staticVersion");
            bean.setIsDelete(0);
            TbConfig config = ServiceContainer.TB_CONFIG_SERVICE.selectOneTableByBean(connection, bean);
            if (config != null) {
                SERVLET_CONTEXT.setAttribute("staticVersion", config.getConfigValue());
            } else {
                SERVLET_CONTEXT.setAttribute("staticVersion", "0");
                bean.setConfigValue("0");
                bean.setConfigComment("静态资源版本");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                bean.setCreateTime(now);
                bean.setUpdateTime(now);
                ServiceContainer.TB_CONFIG_SERVICE.insertIntoTable(connection, bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            ServiceContainer.TB_CONFIG_SERVICE.updateTable(connection, config);
        } else {
            SERVLET_CONTEXT.setAttribute("staticVersion", "0");
            bean.setConfigValue("0");
            bean.setConfigComment("静态资源版本");
            bean.setCreateTime(now);
            bean.setUpdateTime(now);
            ServiceContainer.TB_CONFIG_SERVICE.insertIntoTable(connection, bean);
        }
    }

    private void initConnectionPool() {
        Properties properties = PropertiesUtil.getProperties("db.properties");
        String poolNamesStr = properties.getProperty("pool_names");
        String[] poolNames = poolNamesStr.split(",");
        int length = poolNames.length;
        HikariConfig masterConfig = initConfig(properties, poolNames[0]);
        List<HikariConfig> slaveConfigList = null;
        if (length > 1) {
            slaveConfigList = new ArrayList<HikariConfig>(length - 1);
            for (int i = 1; i < length; i++) {
                String poolName = poolNames[i];
                slaveConfigList.add(initConfig(properties, poolName));
            }
        }
        ConnectionPool.init(masterConfig, slaveConfigList);
    }

    private HikariConfig initConfig(Properties properties, String poolName) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMinimumIdle(Integer.parseInt(properties.getProperty("minimumIdle")));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("maximumPoolSize")));
        config.setPoolName(poolName);
        config.setJdbcUrl(properties.getProperty(poolName + "_jdbcUrl"));
        config.setUsername(properties.getProperty(poolName + "_username"));
        config.setPassword(properties.getProperty(poolName + "_password"));
        return config;
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
                Class<?> c = RequestMappingContainer.getRequestMapping(uri);
                if (c != null) {
                    executor = (BaseExecutor) c.getDeclaredConstructor().newInstance();
                } else {
                    executor = new ResourceNotFoundExecutor();
                }
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
