package com.github.automain.util;

import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import org.apache.commons.io.FileUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SystemUtil {

    private static Map<String, Logger> LOGGER_MAP = new HashMap<String, Logger>();

    /**
     * 初始化连接池
     */
    public static void initConnectionPool() {
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

    private static HikariConfig initConfig(Properties properties, String poolName) {
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

    /**
     * 初始化redis连接池
     */
    public static void initJedisPool() {
        Properties redisConfig = PropertiesUtil.getProperties("redis.properties");
        boolean openRedis = Boolean.parseBoolean(redisConfig.getProperty("openRedis", "false"));
        if (openRedis) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.parseInt(redisConfig.getProperty("maxTotal", "150")));
            config.setMaxIdle(Integer.parseInt(redisConfig.getProperty("maxIdle", "30")));
            config.setMinIdle(Integer.parseInt(redisConfig.getProperty("minIdle", "10")));
            config.setMaxWaitMillis(Long.parseLong(redisConfig.getProperty("maxWaitMillis", "3000")));
            config.setTestOnBorrow(Boolean.parseBoolean(redisConfig.getProperty("testOnBorrow", "false")));
            config.setTestOnReturn(Boolean.parseBoolean(redisConfig.getProperty("testOnReturn", "true")));
            config.setTestWhileIdle(Boolean.parseBoolean(redisConfig.getProperty("testWhileIdle", "true")));
            config.setMinEvictableIdleTimeMillis(Long.parseLong(redisConfig.getProperty("minEvictableIdleTimeMillis", "60000")));
            config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(redisConfig.getProperty("softMinEvictableIdleTimeMillis", "1000")));
            config.setTimeBetweenEvictionRunsMillis(Long.parseLong(redisConfig.getProperty("setTimeBetweenEvictionRunsMillis", "1000")));
            config.setNumTestsPerEvictionRun(Integer.parseInt(redisConfig.getProperty("setNumTestsPerEvictionRun", "100")));
            String host = redisConfig.getProperty("host");
            int port = Integer.parseInt(redisConfig.getProperty("port", "6379"));
            int timeout = Integer.parseInt(redisConfig.getProperty("timeout", "5000"));
            RedisUtil.setPool(new JedisPool(config, host, port, timeout));
        }
    }

    /**
     *  初始化log配置
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
    public static long getNowSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 当前系统是否为windows
     * @return
     */
    public static boolean checkIsWindows() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().startsWith("winbows");
    }

    /**
     * 关闭redis连接和数据库连接
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

}
