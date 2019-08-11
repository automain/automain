package com.github.automain.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static Map<String, String> PROPERTIES_MAP = null;

    static {
        reloadProperties();
    }

    public static void reloadProperties() {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        File propertiesFile = new File(PropertiesUtil.class.getResource("/").getPath().replace("test-classes", "classes") + File.separator + "application.properties");
        if (SystemUtil.checkFileAvailable(propertiesFile)) {
            try (InputStream is = new FileInputStream(propertiesFile)) {
                Properties properties = new Properties();
                properties.load(is);
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    propertiesMap.put(entry.getKey().toString(), entry.getValue().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PROPERTIES_MAP = propertiesMap;
        }
    }

    public static String getStringProperty(String key, String defaultValud) {
        return PROPERTIES_MAP.getOrDefault(key, defaultValud);
    }

    public static String getStringProperty(String key) {
        return getStringProperty(key, null);
    }

    public static int getIntProperty(String key, String defaultValud) {
        return Integer.parseInt(getStringProperty(key, defaultValud));
    }

    public static int getIntProperty(String key) {
        return getIntProperty(key, "0");
    }

    public static long getLongProperty(String key, String defaultValud) {
        return Long.parseLong(getStringProperty(key, defaultValud));
    }

    public static long getLongProperty(String key) {
        return getLongProperty(key, "0");
    }

    public static boolean getBooleanProperty(String key, String defaultValud) {
        return Boolean.parseBoolean(getStringProperty(key, defaultValud));
    }

    public static boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, "false");
    }

}
