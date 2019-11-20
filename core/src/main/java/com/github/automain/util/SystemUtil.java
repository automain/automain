package com.github.automain.util;

import com.github.automain.bean.SysFile;
import com.github.automain.dao.SysFileDao;
import com.github.fastjdbc.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SystemUtil {

    public static final Set<String> ALLOW_ORIGIN = Set.of(PropertiesUtil.getStringProperty("app.allowOrigin").split(","));
    private static final String JDBC_URL = PropertiesUtil.getStringProperty("db.master_jdbcUrl");
    public static final String DATABASE_NAME = JDBC_URL.substring(JDBC_URL.lastIndexOf("/") + 1, JDBC_URL.indexOf("?"));
    public static final List<String> IMG_TYPES = List.of("bmp", "jpg", "jpeg", "png", "gif");
    public static final String UPLOADS_PATH = PropertiesUtil.getStringProperty("app.uploadsPath");
    private static final String CDN_PATH = PropertiesUtil.getStringProperty("app.cdnPath");

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
     * 保存base64到文件
     *
     * @param connection
     * @param base64
     * @param fileExtension
     * @return
     * @throws SQLException
     */
    public static Integer saveFileByBase64(Connection connection, String base64, String fileExtension) throws Exception {
        if (fileExtension != null) {
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
            String filePath = "/" + DateUtil.getNow(DateUtil.DATE_FORMATTER) + "/" + newFileName;
            String absolutePath = UPLOADS_PATH + filePath;
            base6ToFile(base64, absolutePath);
            File file = new File(absolutePath);
            String fileMd5 = EncryptUtil.MD5(file);
            int fileSize = (int) file.length();
            int imageWidth = 0;
            int imageHeight = 0;
            if (IMG_TYPES.contains(fileExtension)) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    BufferedImage image = ImageIO.read(fis);
                    imageWidth = image.getWidth();
                    imageHeight = image.getHeight();
                }
            }
            int now = DateUtil.getNow();
            SysFile uploadFile = new SysFile()
                    .setGid(UUID.randomUUID().toString())
                    .setCreateTime(now)
                    .setUpdateTime(now)
                    .setIsValid(1)
                    .setFileExtension(fileExtension)
                    .setFilePath(filePath)
                    .setFileSize(fileSize)
                    .setFileMd5(fileMd5)
                    .setImageHeight(imageHeight)
                    .setImageWidth(imageWidth);
            return SysFileDao.insertIntoTableReturnId(connection, uploadFile);
        }
        return 0;
    }


    /**
     * base64转文件
     *
     * @param base64
     * @param filePath
     */
    private static void base6ToFile(String base64, String filePath) throws IOException {
        File file = new File(filePath);
        if (SystemUtil.mkdirs(file.getParentFile())) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] bytes = EncryptUtil.BASE64Decode(base64);
                fos.write(bytes);
            }
        }
    }

    /**
     * 文件转为base64
     *
     * @param relativeFilePath
     * @return
     */
    public static String fileToBase64(String relativeFilePath) throws IOException {
        String filePath = UPLOADS_PATH + relativeFilePath;
        File file = new File(filePath);
        if (SystemUtil.checkFileAvailable(file)) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[(int) file.length()];
                if (fis.read(buffer) > -1) {
                    return EncryptUtil.BASE64Encode(buffer);
                }
            }
        }
        return null;
    }

    /**
     * 获取文件下载路径
     *
     * @param request
     * @param filePath
     * @return
     */
    private static String concatPathForFile(HttpServletRequest request, String filePath) {
        return StringUtils.isNotBlank(CDN_PATH) ? CDN_PATH + filePath : request.getContextPath() + "/uploads" + filePath;
    }
}
