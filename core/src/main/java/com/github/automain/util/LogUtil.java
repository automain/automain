package com.github.automain.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogUtil {

    private static Map<String, Logger> LOGGER_MAP = new HashMap<String, Logger>();

    static {
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

}
