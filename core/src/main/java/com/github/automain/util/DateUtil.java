package com.github.automain.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //==============================Date To String==============================

    /**
     * LocalDateTime类型转为日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(LocalDateTime date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return convertDateToString(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 时间戳(秒)类型转为日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Integer date, String pattern) {
        return convertDateToString(convertToLocalDateTime(date), pattern);
    }

    /**
     * LocalDateTime类型转为日期字符串
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String convertDateToString(LocalDateTime date, DateTimeFormatter formatter) {
        if (date == null || formatter == null) {
            return null;
        }
        return date.format(formatter);
    }

    /**
     * 时间戳(秒)类型转为日期字符串
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String convertDateToString(Integer date, DateTimeFormatter formatter) {
        return convertDateToString(convertToLocalDateTime(date), formatter);
    }

    //==============================String To Date==============================

    /**
     * 字符串转LocalDateTime日期类型
     *
     * @param date
     * @param pattern
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return convertStringToLocalDateTime(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 字符串转时间戳(秒)日期类型
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Integer convertStringToTimestamp(String date, String pattern) {
        return convertToTimestamp(convertStringToLocalDateTime(date, pattern));
    }

    /**
     * 字符串转LocalDateTime日期类型
     *
     * @param date
     * @param formatter
     * @return
     */
    public static LocalDateTime convertStringToLocalDateTime(String date, DateTimeFormatter formatter) {
        if (date == null || formatter == null) {
            return null;
        }
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * 字符串转时间戳(秒)日期类型
     *
     * @param date
     * @param formatter
     * @return
     */
    public static Integer convertStringToTimestamp(String date, DateTimeFormatter formatter) {
        return convertToTimestamp(convertStringToLocalDateTime(date, formatter));
    }

    //==============================Timestamp To LocalDateTime==============================

    /**
     * 时间戳(秒)类型转为LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertToLocalDateTime(Integer date) {
        return date == null ? null : LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.systemDefault());
    }

    //==============================LocalDateTime To Timestamp==============================

    /**
     * LocalDateTime类型转为时间戳(秒)
     *
     * @param date
     * @return
     */
    public static Integer convertToTimestamp(LocalDateTime date) {
        return date == null ? null : (int) date.atZone(ZoneOffset.systemDefault()).toInstant().getEpochSecond();
    }

    //==============================Day Start==============================

    /**
     * 获取给定LocalDateTime类型日期0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime date) {
        return date == null ? null : LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0, 0);
    }

    /**
     * 获取给定时间戳(秒)0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getDayStart(Integer date) {
        return getDayStart(convertToLocalDateTime(date));
    }

    //==============================Next Day Start==============================

    /**
     * 获取给定LocalDateTime类型后一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getNextDayStart(LocalDateTime date) {
        return date == null ? null : getDayStart(date.plusDays(1));
    }

    /**
     * 获取给定时间戳(秒)后一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getNextDayStart(Integer date) {
        return getNextDayStart(convertToLocalDateTime(date));
    }

    //==============================Last Day Start==============================

    /**
     * 获取给定LocalDateTime类型前一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayStart(LocalDateTime date) {
        return date == null ? null : getDayStart(date.minusDays(1));
    }

    /**
     * 获取给定时间戳(秒)前一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayStart(Integer date) {
        return getLastDayStart(convertToLocalDateTime(date));
    }

    //==============================Days Between Two Time==============================

    /**
     * 获取两个LocalDateTime类型日期相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return getDaysBetween(convertToTimestamp(startDate), convertToTimestamp(endDate));
    }

    /**
     * 获取两个时间戳类型(秒)相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Integer startDate, Integer endDate) {
        if (startDate == null || endDate == null) {
            return Integer.MAX_VALUE;
        }
        return (endDate - startDate) / 86400;
    }

    //==============================First Day Of Month==============================

    /**
     * 获取给定LocalDateTime类型时间所在月的第一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getFirstDayOfMonth(LocalDateTime date) {
        return date == null ? null : LocalDateTime.of(date.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 获取给定时间戳(秒)所在月的第一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getFirstDayOfMonth(Integer date) {
        return getFirstDayOfMonth(convertToLocalDateTime(date));
    }

    //==============================Last Day Of Month==============================

    /**
     * 获取给定LocalDateTime类型时间所在月的最后一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayOfMonth(LocalDateTime date) {
        return date == null ? null : LocalDateTime.of(date.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 获取给定时间戳(秒)所在月的最后一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayOfMonth(Integer date) {
        return getLastDayOfMonth(convertToLocalDateTime(date));
    }

    //==============================First Day Of Week==============================

    /**
     * 获取给定LocalDateTime类型时间所在周的第一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getFirstDayOfWeek(LocalDateTime date) {
        return date == null ? null : date.minusDays(date.getDayOfWeek().getValue() - 1);
    }

    /**
     * 获取给定时间戳(秒)所在周的第一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getFirstDayOfWeek(Integer date) {
        return getFirstDayOfWeek(convertToLocalDateTime(date));
    }

    //==============================Last Day Of Week==============================

    /**
     * 获取给定LocalDateTime类型时间所在周的最后一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayOfWeek(LocalDateTime date) {
        return date == null ? null : date.plusDays(7 - date.getDayOfWeek().getValue());
    }

    /**
     * 获取给定时间戳(秒)所在周的最后一天
     *
     * @param date
     * @return
     */
    public static LocalDateTime getLastDayOfWeek(Integer date) {
        return getLastDayOfWeek(convertToLocalDateTime(date));
    }

    //==============================Check Intersection Common==============================

    /**
     * 公用比较方法
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable> boolean checkIntersectionCommon(T startDate1, T endDate1, T startDate2, T endDate2) {
        if (startDate1 == null || endDate1 == null || startDate2 == null || endDate2 == null) {
            return false;
        }
        return startDate1.compareTo(endDate2) < 0 && startDate2.compareTo(endDate1) < 0;
    }
    //==============================Check Intersection==============================

    /**
     * 检查两个LocalDateTime类型时间段是否有交集
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static boolean checkIntersection(LocalDateTime startDate1, LocalDateTime endDate1, LocalDateTime startDate2, LocalDateTime endDate2) {
        return checkIntersectionCommon(startDate1, endDate1, startDate2, endDate2);
    }

    /**
     * 检查两个时间段戳(秒)是否有交集
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static boolean checkIntersection(Integer startDate1, Integer endDate1, Integer startDate2, Integer endDate2) {
        return checkIntersectionCommon(startDate1, endDate1, startDate2, endDate2);
    }

    //==============================Now==============================

    /**
     * 获取系统当前时间字符串
     *
     * @param pattern
     * @return
     */
    public static String getNow(String pattern) {
        return convertDateToString(LocalDateTime.now(), pattern);
    }

    /**
     * 获取系统当前时间字符串
     *
     * @param formatter
     * @return
     */
    public static String getNow(DateTimeFormatter formatter) {
        return convertDateToString(LocalDateTime.now(), formatter);
    }

    /**
     * 获取系统当前时间戳(秒)
     *
     * @return
     */
    public static int getNow() {
        return (int) (System.currentTimeMillis() / 1000);
    }

}
