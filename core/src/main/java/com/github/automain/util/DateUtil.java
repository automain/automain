package com.github.automain.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    //==============================Date To String==============================

    /**
     * Date类型转为日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Calendar类型转为日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Calendar date, String pattern) {
        return convertDateToString(date.getTime(), pattern);
    }

    /**
     * 时间戳(毫秒)类型转为日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Long date, String pattern) {
        return convertDateToString(convertToDate(date), pattern);
    }

    //==============================String To Date==============================

    /**
     * 字符串转Date日期类型
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date convertStringToDate(String date, String pattern) throws ParseException {
        if (date == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * 字符串转Calendar日期类型
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Calendar convertStringToCalendar(String date, String pattern) throws ParseException {
        return convertToCalendar(convertStringToDate(date, pattern));
    }

    /**
     * 字符串转时间戳日期类型
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Long convertStringToLong(String date, String pattern) throws ParseException {
        Date convertDate = convertStringToDate(date, pattern);
        return convertDate == null ? null : convertDate.getTime();
    }

    //==============================Time To Date==============================

    /**
     * 时间类型转为Date
     *
     * @param date
     * @return
     */
    public static Date convertToDate(Calendar date) {
        return date == null ? null : date.getTime();
    }

    /**
     * 时间类型转为Date
     *
     * @param date
     * @return
     */
    public static Date convertToDate(Long date) {
        return date == null ? null : new Date(date);
    }

    //==============================Time To Calendar==============================

    /**
     * 时间类型转为Calendar
     *
     * @param date
     * @return
     */
    public static Calendar convertToCalendar(Date date) {
        return date == null ? null : convertToCalendar(date.getTime());
    }

    /**
     * 时间类型转为Calendar
     *
     * @param date
     * @return
     */
    public static Calendar convertToCalendar(Long date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c;
    }

    //==============================Time To Long==============================

    /**
     * 时间类型转为时间戳
     *
     * @param date
     * @return
     */
    public static Long convertToLong(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * 时间类型转为时间戳
     *
     * @param date
     * @return
     */
    public static Long convertToLong(Calendar date) {
        return date == null ? null : date.getTimeInMillis();
    }

    //==============================Min Day Calendar==============================

    /**
     * 获取给定日期0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getMinDayCalendar(Date date) {
        return getMinDayCalendar(convertToCalendar(date));
    }

    /**
     * 获取给定日期0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getMinDayCalendar(Calendar date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * 获取给定日期0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getMinDayCalendar(Long date) {
        return getMinDayCalendar(convertToCalendar(date));
    }

    //==============================Next Day Calendar==============================

    /**
     * 获取给定日期后一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getNextDayCalendar(Date date) {
        return getNextDayCalendar(convertToCalendar(date));
    }

    /**
     * 获取给定日期后一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getNextDayCalendar(Calendar date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) + 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * 获取给定日期后一天0点0分0秒
     *
     * @param date
     * @return
     */
    public static Calendar getNextDayCalendar(Long date) {
        return getNextDayCalendar(convertToCalendar(date));
    }

    //==============================Days Between Two Time==============================

    /**
     * 获取两个日期相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        return getDaysBetween(convertToLong(startDate), convertToLong(endDate));
    }

    /**
     * 获取两个日期相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Calendar startDate, Calendar endDate) {
        return getDaysBetween(convertToLong(startDate), convertToLong(endDate));
    }

    /**
     * 获取两个日期相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysBetween(Long startDate, Long endDate) {
        if (startDate == null || endDate == null) {
            return Integer.MAX_VALUE;
        }
        return (int) ((endDate - startDate) / ONE_DAY);
    }
    //==============================Now==============================

    /**
     * 获取系统当前时间字符串
     *
     * @param pattern
     * @return
     */
    public static String getNow(String pattern) {
        if (pattern == null) {
            return null;
        }
        return convertDateToString(System.currentTimeMillis(), pattern);
    }

    /**
     * 获取系统当前时间戳(秒)
     *
     * @return
     */
    public static int getNowSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    //==============================Common Get Day==============================

    /**
     * 获取给定条件的日期
     *
     * @param date
     * @param field
     * @param dayNo
     * @return
     */
    private static Calendar getGivenDayOfField(Long date, int field, int dayNo) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTimeInMillis(date);
        c.set(field, dayNo);
        return c;
    }
    //==============================First Day Of Month==============================

    /**
     * 获取给定时间所在月的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfMonth(Date date) {
        return date == null ? null : getFirstDayOfMonth(date.getTime());
    }

    /**
     * 获取给定时间所在月的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfMonth(Calendar date) {
        return date == null ? null : getFirstDayOfMonth(date.getTimeInMillis());
    }

    /**
     * 获取给定时间所在月的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfMonth(Long date) {
        return getGivenDayOfField(date, Calendar.DAY_OF_MONTH, 1);
    }

    //==============================Last Day Of Month==============================

    /**
     * 获取给定时间所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfMonth(Date date) {
        return getLastDayOfMonth(convertToCalendar(date));
    }

    /**
     * 获取给定时间所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfMonth(Calendar date) {
        return date == null ? null : getGivenDayOfField(date.getTimeInMillis(), Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获取给定时间所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfMonth(Long date) {
        return getLastDayOfMonth(convertToCalendar(date));
    }

    //==============================First Day Of Week==============================

    /**
     * 获取给定时间所在周的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfWeek(Date date) {
        return date == null ? null : getFirstDayOfWeek(date.getTime());
    }

    /**
     * 获取给定时间所在周的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfWeek(Calendar date) {
        return date == null ? null : getFirstDayOfWeek(date.getTimeInMillis());
    }

    /**
     * 获取给定时间所在周的第一天
     *
     * @param date
     * @return
     */
    public static Calendar getFirstDayOfWeek(Long date) {
        return getGivenDayOfField(date, Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    //==============================Last Day Of Week==============================

    /**
     * 获取给定时间所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfWeek(Date date) {
        return date == null ? null : getLastDayOfWeek(date.getTime());
    }

    /**
     * 获取给定时间所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfWeek(Calendar date) {
        return date == null ? null : getLastDayOfWeek(date.getTimeInMillis());
    }

    /**
     * 获取给定时间所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Calendar getLastDayOfWeek(Long date) {
        return getGivenDayOfField(date, Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
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
     * 检查两个时间段是否有交集
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static boolean checkIntersection(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        return checkIntersectionCommon(startDate1, endDate1, startDate2, endDate2);
    }

    /**
     * 检查两个时间段是否有交集
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static boolean checkIntersection(Calendar startDate1, Calendar endDate1, Calendar startDate2, Calendar endDate2) {
        return checkIntersectionCommon(startDate1, endDate1, startDate2, endDate2);
    }

    /**
     * 检查两个时间段是否有交集
     *
     * @param startDate1
     * @param endDate1
     * @param startDate2
     * @param endDate2
     * @return
     */
    public static boolean checkIntersection(Long startDate1, Long endDate1, Long startDate2, Long endDate2) {
        return checkIntersectionCommon(startDate1, endDate1, startDate2, endDate2);
    }
}
