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

    static {
        reloadProperties();
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
    public static final Properties CONFIG_PROPERTIES = getProperties("config.properties");
    public static final String ENVIRONMENT = CONFIG_PROPERTIES.getProperty("environment");
    private static final int SESSION_EXPIRE_SECONDS = Integer.parseInt(CONFIG_PROPERTIES.getProperty("sessionExpireSeconds"));
    public static final long SESSION_EXPIRE_MILLISECOND = SESSION_EXPIRE_SECONDS * 1000;
    public static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 300;
    public static final long CACHE_EXPIRE_MILLISECOND = CACHE_EXPIRE_SECONDS * 1000;
    public static final String SECURITY_KEY = CONFIG_PROPERTIES.getProperty("securityKey");
    public static final String UPLOADS_PATH = CONFIG_PROPERTIES.getProperty("uploadsPath");
    public static final boolean OPEN_CACHE = Boolean.parseBoolean(CONFIG_PROPERTIES.getProperty("openCache", "false"));
    public static final String CACHE_PATH = CONFIG_PROPERTIES.getProperty("cachePath");

    public static Properties getProperties(String fileName) {
        return PROPERTIES_MAP.get(fileName);
    }

}
