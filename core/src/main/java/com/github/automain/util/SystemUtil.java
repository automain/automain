package com.github.automain.util;

import com.github.fastjdbc.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

public class SystemUtil {

    private static ScheduledExecutorService SCHEDULE_THREAD_POOL = null;
    private static final boolean OPEN_SCHEDULE = PropertiesUtil.getBooleanProperty("app.openSchedule");
    public static final String PROJECT_HOST = PropertiesUtil.getStringProperty("app.projectHost");
    public static final Set<String> ALLOW_ORIGIN = Set.of(PropertiesUtil.getStringProperty("app.allowOrigin").split(","));
    private static final String JDBC_URL = PropertiesUtil.getStringProperty("db.master_jdbcUrl");
    public static final String DATABASE_NAME = JDBC_URL.substring(JDBC_URL.lastIndexOf("/") + 1, JDBC_URL.indexOf("?"));
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
        return config;
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
     * 创建目录
     *
     * @param file
     * @return
     */
    public static boolean mkdirs(File file) {
        return file != null && ((file.exists() && file.isDirectory()) || file.mkdirs());
    }

    /**
     * 关闭redis连接和数据库连接
     *
     * @param jedis
     * @param connection
     */
    public static void closeJedisAndConnection(Jedis jedis, Connection connection) {
        try {
            if (jedis != null) {
                jedis.close();
            }
            ConnectionPool.close(connection);
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
//    public static synchronized void reloadSchedule(ConnectionBean connection) throws SQLException {
//        if (OPEN_SCHEDULE) {
//            if (SCHEDULE_THREAD_POOL != null) {
//                SCHEDULE_THREAD_POOL.shutdown();
//                SCHEDULE_THREAD_POOL = null;
//            }
//            TbSchedule bean = new TbSchedule();
//            bean.setIsDelete(0);
//            List<TbSchedule> scheduleList = ServiceDaoContainer.TB_SCHEDULE_SERVICE.selectTableByBean(connection, bean);
//            int size = scheduleList.size();
//            if (size > 0) {
//                SCHEDULE_THREAD_POOL = Executors.newScheduledThreadPool(size);
//                for (TbSchedule schedule : scheduleList) {
//                    startSchedule(schedule);
//                }
//            }
//        }
//    }
//
//    private static void startSchedule(TbSchedule schedule) {
//        long initialDelay = 0L;
//        long period = schedule.getDelayTime();
//        long now = DateUtil.getNow();
//        long start = schedule.getStartExecuteTime().getTime() / 1000;
//        long diff = now - start;
//        if (diff > 0) {
//            if (diff > period) {
//                initialDelay = period - (diff % period);
//            } else {
//                initialDelay = period - diff;
//            }
//            SCHEDULE_THREAD_POOL.scheduleAtFixedRate(new ScheduleThread(schedule.getScheduleUrl(), period), initialDelay, period, TimeUnit.SECONDS);
//        }
//    }

}
