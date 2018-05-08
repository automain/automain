package com.github.automain.util;

import com.github.fastjdbc.bean.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SystemUtil {

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
}
