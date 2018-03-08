package com.github.automain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LocationUtil {

    // 地球半径
    private static final BigDecimal RADIUS = new BigDecimal(6370996.81);
    // 经度最小值
    private static final BigDecimal LONGITUDE_MIN = new BigDecimal(-180);
    // 经度最大值
    private static final BigDecimal LONGITUDE_MAX = new BigDecimal(180);
    // 纬度最小值
    private static final BigDecimal LATITUDE_MIN = new BigDecimal(-74);
    // 纬度最大值
    private static final BigDecimal LATITUDE_MAN = new BigDecimal(74);
    // 角度最大值
    private static final BigDecimal ANGLE_MAX = new BigDecimal(180);
    // 圆周率
    private static final BigDecimal PI = new BigDecimal(Math.PI);

    private static BigDecimal longitudeRadian(BigDecimal angle, BigDecimal min, BigDecimal max) {
        BigDecimal difference = max.subtract(min);
        while (angle.compareTo(max) > 0) {
            angle = angle.subtract(difference);
        }
        while (angle.compareTo(min) < 0) {
            angle = angle.add(difference);
        }
        return angle;
    }

    private static BigDecimal latitudeRadian(BigDecimal angle, BigDecimal min, BigDecimal max) {
        if (angle.compareTo(min) < 0) {
            angle = min;
        }
        return angle.compareTo(max) > 0 ? max : angle;
    }

    // 获取两个经纬度之间的距离(单位:米)
    public static BigDecimal getDistance(BigDecimal longitude1, BigDecimal latitude1, BigDecimal longitude2, BigDecimal latitude2) {
        BigDecimal lng1 = PI.multiply(longitudeRadian(longitude1, LONGITUDE_MIN, LONGITUDE_MAX)).divide(ANGLE_MAX, RoundingMode.HALF_UP);
        BigDecimal lng2 = PI.multiply(longitudeRadian(longitude2, LONGITUDE_MIN, LONGITUDE_MAX)).divide(ANGLE_MAX, RoundingMode.HALF_UP);
        BigDecimal lat1 = PI.multiply(longitudeRadian(latitude1, LATITUDE_MIN, LATITUDE_MAN)).divide(ANGLE_MAX, RoundingMode.HALF_UP);
        BigDecimal lat2 = PI.multiply(longitudeRadian(latitude2, LATITUDE_MIN, LATITUDE_MAN)).divide(ANGLE_MAX, RoundingMode.HALF_UP);
        BigDecimal latSin1 = new BigDecimal(Math.sin(lat1.doubleValue()));
        BigDecimal latSin2 = new BigDecimal(Math.sin(lat2.doubleValue()));
        BigDecimal latCos1 = new BigDecimal(Math.cos(lat1.doubleValue()));
        BigDecimal latCos2 = new BigDecimal(Math.cos(lat2.doubleValue()));
        BigDecimal lngCos = new BigDecimal(Math.cos(lng2.subtract(lng1).doubleValue()));
        BigDecimal arcCos = new BigDecimal(Math.acos(latSin1.multiply(latSin2).add(latCos1.multiply(latCos2).multiply(lngCos)).doubleValue()));
        return RADIUS.multiply(arcCos).setScale(2, RoundingMode.HALF_UP);
    }
    
}
