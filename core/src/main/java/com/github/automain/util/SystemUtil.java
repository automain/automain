package com.github.automain.util;

import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.thread.ScheduleThread;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SystemUtil {

    private static Logger LOGGER = null;
    private static ScheduledExecutorService SCHEDULE_THREAD_POOL = null;
    private static final boolean OPEN_SCHEDULE = PropertiesUtil.getBooleanProperty("app.openSchedule");
    public static final String PROJECT_HOST = PropertiesUtil.getStringProperty("app.projectHost");
    private static final String url = PropertiesUtil.getStringProperty("db.master_jdbcUrl");
    public static final String DATABASE_NAME = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
    public static final Map<String, String> API_KEY_MAP = new HashMap<String, String>();
    /**
     * 初始化连接池
     */
    public static void initConnectionPool() {
        // 初始化主数据源
        HikariConfig masterConfig = initConfig("master");
        masterConfig.setAutoCommit(false);
        HikariDataSource masterPool = new HikariDataSource(masterConfig);
        // 初始化从数据源
        Map<String, DataSource> slavePoolMap = new HashMap<String, DataSource>();
        String poolNamesStr = PropertiesUtil.getStringProperty("db.slavePoolNames");
        if (StringUtils.isNotBlank(poolNamesStr)) {
            String[] poolNames = poolNamesStr.split(",");
            for (String poolName : poolNames) {
                slavePoolMap.put(poolName, new HikariDataSource(initConfig(poolName)));
            }
        }
        ConnectionPool.init(masterPool, slavePoolMap);
    }

    private static HikariConfig initConfig(String poolName) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMinimumIdle(PropertiesUtil.getIntProperty("db.minimumIdle"));
        config.setMaximumPoolSize(PropertiesUtil.getIntProperty("db.maximumPoolSize"));
        config.setPoolName(poolName);
        config.setJdbcUrl(PropertiesUtil.getStringProperty("db." + poolName + "_jdbcUrl"));
        config.setUsername(PropertiesUtil.getStringProperty("db." + poolName + "_username"));
        config.setPassword(PropertiesUtil.getStringProperty("db." + poolName + "_password"));
        config.setAllowPoolSuspension(true);
        return config;
    }

    /**
     * 初始化log配置
     */
    public static void initLogConfig() {
        try {
            Logger logger = Logger.getLogger("system");
            Handler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);//从高到低->OFF\SEVERE\WARNING\INFO\CONFIG\FINE\FINER\FINEST\ALL
            handler.setEncoding(PropertiesUtil.DEFAULT_CHARSET);
            handler.setFormatter(new LogFormatter());
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);//从高到低->OFF\SEVERE\WARNING\INFO\CONFIG\FINE\FINER\FINEST\ALL
            logger.setUseParentHandlers(false);
            LOGGER = logger;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private static class LogFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            return "[" + DateUtil.convertDateToString(record.getMillis(), "yyyy-MM-dd HH:mm:ss.SSS") +
                    "][" + record.getSourceClassName() + ":" + record.getSourceMethodName() +
                    "][" + record.getThreadID() + "]" + System.lineSeparator() + "[" + record.getLevel() + "]" + record.getMessage() + System.lineSeparator();
        }
    }

    /**
     * 判断文件是否可用
     *
     * @param file
     * @return
     */
    public static boolean checkFileAvailable(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    /**
     * 当前系统是否为windows
     *
     * @return
     */
    public static boolean checkIsWindows() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().startsWith("winbows");
    }

    /**
     * 关闭redis连接和数据库连接
     *
     * @param jedis
     * @param connection
     */
    public static void closeJedisAndConnectionBean(Jedis jedis, ConnectionBean connection) {
        try {
            if (jedis != null) {
                jedis.close();
            }
            ConnectionPool.closeConnectionBean(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新定时任务
     *
     * @param connection
     * @throws SQLException
     */
    public static synchronized void reloadSchedule(ConnectionBean connection) throws SQLException {
        if (OPEN_SCHEDULE) {
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
                    startSchedule(schedule);
                }
            }
        }
    }

    private static void startSchedule(TbSchedule schedule) {
        long initialDelay = 0L;
        long period = schedule.getDelayTime();
        long now = DateUtil.getNowSecond();
        long start = schedule.getStartExecuteTime().getTime() / 1000;
        long diff = now - start;
        if (diff > 0) {
            if (diff > period) {
                initialDelay = period - (diff % period);
            } else {
                initialDelay = period - diff;
            }
            SCHEDULE_THREAD_POOL.scheduleAtFixedRate(new ScheduleThread(schedule.getScheduleUrl(), period), initialDelay, period, TimeUnit.SECONDS);
        }
    }
}
