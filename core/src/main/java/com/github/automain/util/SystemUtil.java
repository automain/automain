package com.github.automain.util;

import java.io.File;
import java.util.UUID;

public class SystemUtil {

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
}
