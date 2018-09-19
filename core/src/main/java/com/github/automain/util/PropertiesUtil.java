package com.github.automain.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtil {

    private static Map<String, Properties> PROPERTIES_MAP = null;

    private static final String CLASS_PATH = PropertiesUtil.class.getResource("/").getPath().replace("test-classes", "classes");

    public static final String WEB_INFO_PATH = CLASS_PATH.replace("/classes/", "");

    public static final Map<String, String> API_KEY_MAP = new ConcurrentHashMap<String, String>();

    static {
        reloadProperties();
        Properties properties = getProperties("apikey.properties");
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            API_KEY_MAP.put(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    public static void reloadProperties() {
        ConcurrentHashMap<String, Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();
        File resources = new File(CLASS_PATH);
        File[] files = resources.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".properties")) {
                    try (InputStream is = new FileInputStream(file)) {
                        Properties properties = new Properties();
                        properties.load(is);
                        propertiesMap.put(file.getName(), properties);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            PROPERTIES_MAP = propertiesMap;
        }
    }

    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final Properties CONFIG_PROPERTIES = getProperties("config.properties");
    public static final String ENVIRONMENT = CONFIG_PROPERTIES.getProperty("environment");
    private static final int SESSION_EXPIRE_SECONDS = Integer.parseInt(CONFIG_PROPERTIES.getProperty("sessionExpireSeconds"));
    public static final long SESSION_EXPIRE_MILLISECOND = SESSION_EXPIRE_SECONDS * 1000;
    public static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 300;
    public static final long CACHE_EXPIRE_MILLISECOND = CACHE_EXPIRE_SECONDS * 1000;
    public static final String SECURITY_KEY = CONFIG_PROPERTIES.getProperty("securityKey");
    public static final String UPLOADS_PATH = CONFIG_PROPERTIES.getProperty("uploadsPath");
    public static final String CDN_PATH = CONFIG_PROPERTIES.getProperty("cdnPath");
    public static final boolean OPEN_CACHE = Boolean.parseBoolean(CONFIG_PROPERTIES.getProperty("openCache", "false"));
    public static final String CACHE_PATH = CONFIG_PROPERTIES.getProperty("cachePath");
    public static final boolean OPEN_SCHEDULE = Boolean.parseBoolean(CONFIG_PROPERTIES.getProperty("openSchedule", "false"));
    public static final String PROJECT_HOST = CONFIG_PROPERTIES.getProperty("projectHost");

    public static final Properties DB_PROPERTIES = getProperties("db.properties");
    private static final String url = DB_PROPERTIES.getProperty("master_jdbcUrl");
    public static final String DATABASE_NAME = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));

    private static final Properties ZK_PROPERTIES = getProperties("zk.properties");
    public static final String ZK_SERVERS = ZK_PROPERTIES.getProperty("servers");
    public static final String DETAULT_NAMESPACE = ZK_PROPERTIES.getProperty("defaultNamespace");
    public static final boolean OPEN_ZOOKEEPER = Boolean.parseBoolean(ZK_PROPERTIES.getProperty("openZookeeper", "false"));
    public static Properties getProperties(String fileName) {
        return PROPERTIES_MAP.get(fileName);
    }

}
