package com.github.automain.util;

import com.github.automain.common.bean.TbConfig;
import com.github.automain.common.bean.TbSchedule;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.common.thread.ScheduleThread;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SystemUtil {

    private static Map<String, Logger> LOGGER_MAP = new HashMap<String, Logger>();

    private static ScheduledExecutorService SCHEDULE_THREAD_POOL = null;

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
        String poolNamesStr = PropertiesUtil.DB_PROPERTIES.getProperty("slave_pool_names");
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
        config.setMinimumIdle(Integer.parseInt(PropertiesUtil.DB_PROPERTIES.getProperty("minimumIdle")));
        config.setMaximumPoolSize(Integer.parseInt(PropertiesUtil.DB_PROPERTIES.getProperty("maximumPoolSize")));
        config.setPoolName(poolName);
        config.setJdbcUrl(PropertiesUtil.DB_PROPERTIES.getProperty(poolName + "_jdbcUrl"));
        config.setUsername(PropertiesUtil.DB_PROPERTIES.getProperty(poolName + "_username"));
        config.setPassword(PropertiesUtil.DB_PROPERTIES.getProperty(poolName + "_password"));
        config.setAllowPoolSuspension(true);
        return config;
    }

    /**
     * 初始化log配置
     */
    public static void initLogConfig() {
        try {
            Properties logProperties = PropertiesUtil.getProperties("log.properties");
            String rootLogger = logProperties.getProperty("rootLogger");
            String[] logNames = rootLogger.split(",");
            LogFormatter formatter = new LogFormatter();
            int logSize = logNames.length;
            for (int i = 0; i < logSize; i++) {
                Logger logger = Logger.getLogger(logNames[i]);
                Level level = getLogLevelByName(logProperties.getProperty("appender." + logNames[i] + ".Threshold"));
                String pattern = logProperties.getProperty("appender." + logNames[i] + ".File");
                if (!pattern.startsWith("%")) {
                    File path = new File(pattern);
                    if (!UploadUtil.mkdirs(path.getParentFile())) {
                        break;
                    }
                }
                int limit = Integer.parseInt(logProperties.getProperty("appender." + logNames[i] + ".MaxFileSize"));
                int count = Integer.parseInt(logProperties.getProperty("appender." + logNames[i] + ".MaxBackupIndex"));
                FileHandler handler = new FileHandler(pattern, limit, count, true);
                handler.setLevel(level);
                handler.setEncoding(PropertiesUtil.DEFAULT_CHARSET);
                handler.setFormatter(formatter);
                logger.addHandler(handler);
                logger.setLevel(level);
                logger.setUseParentHandlers(false);
                LOGGER_MAP.put(logNames[i], logger);
            }
            Logger systemLogger = getLoggerByName("system");
            systemLogger.info("system started");
            if (PropertiesUtil.OPEN_CACHE) {
                File file = new File(PropertiesUtil.CACHE_PATH);
                FileUtils.deleteDirectory(file);
                systemLogger.info("static file cache deleted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLoggerByName(String loggerName) {
        return LOGGER_MAP.get(loggerName);
    }

    private static Level getLogLevelByName(String logLevel) {
        if (logLevel == null) {
            return null;
        }
        String upperLogLevel = logLevel.toUpperCase();
        switch (upperLogLevel) {
            case "OFF":
                return Level.OFF;
            case "SEVERE":
                return Level.SEVERE;
            case "WARNING":
                return Level.WARNING;
            case "INFO":
                return Level.INFO;
            case "CONFIG":
                return Level.CONFIG;
            case "FINE":
                return Level.FINE;
            case "FINER":
                return Level.FINER;
            case "FINEST":
                return Level.FINEST;
            case "ALL":
                return Level.ALL;
            default:
                return Level.INFO;
        }
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
     * 32位全局唯一ID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
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
     * 获取系统当前时间戳(秒)
     *
     * @return
     */
    public static int getNowSecond() {
        return (int) (System.currentTimeMillis() / 1000);
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
     * 刷新静态资源缓存
     *
     * @param servletContext
     * @param connection
     * @throws Exception
     */
    public static void reloadStaticVersion(ServletContext servletContext, ConnectionBean connection) throws Exception {
        TbConfig bean = new TbConfig();
        bean.setConfigKey("staticVersion");
        TbConfig config = ServiceContainer.TB_CONFIG_SERVICE.selectOneTableByBean(connection, bean);
        if (config != null) {
            servletContext.setAttribute("staticVersion", config.getConfigValue());
        } else {
            servletContext.setAttribute("staticVersion", "0");
        }
    }

    /**
     * 刷新定时任务
     *
     * @param connection
     * @throws SQLException
     */
    public static synchronized void reloadSchedule(ConnectionBean connection) throws SQLException {
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
                    startSchedule(schedule);
                }
            }
        }
    }

    private static void startSchedule(TbSchedule schedule) {
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
            SCHEDULE_THREAD_POOL.scheduleAtFixedRate(new ScheduleThread(schedule.getScheduleUrl(), jump), initialDelay, jump, TimeUnit.SECONDS);
        }
    }
}
