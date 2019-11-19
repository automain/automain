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

public class SystemUtil {

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
}
