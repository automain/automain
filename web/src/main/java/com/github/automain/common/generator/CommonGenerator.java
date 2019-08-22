package com.github.automain.common.generator;


import com.github.automain.common.bean.ColumnBean;

public class CommonGenerator {

    public static String convertToJavaName(String dbName, boolean uppercaseFirst) {
        String[] names = dbName.split("_");
        StringBuilder javaName = new StringBuilder();
        for (String name : names) {
            if (name.length() == 0) {
                continue;
            }
            javaName.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
        }
        if (uppercaseFirst) {
            return javaName.toString();
        } else {
            return javaName.substring(0, 1).toLowerCase() + javaName.substring(1);
        }
    }

    public static boolean checkTimeTypeColumn(ColumnBean column) {
        return "Integer".equals(column.getDataType()) && column.getColumnName().toLowerCase().endsWith("time");
    }

}
