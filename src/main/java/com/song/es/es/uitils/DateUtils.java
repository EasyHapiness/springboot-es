package com.song.es.es.uitils;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @Desc
 * @Author
 * @Date 2019/8/16
 */
public class DateUtils {
    final static DateTimeFormatter defaultDateFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final static DateTimeFormatter defaultMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public enum EnumDateUnit {
        MONTH, DAY, YEAR, WEEK;
    }

    /**
     * 计算当前日期到目标日期d1的年数差
     *
     * @param d1 Date类型的日期比较对象
     * @return 年数差值
     */
    public static int calculateYearToNow(Date d1) {
        return calculateYearOfTwoDate(d1, new Date());
    }

    /**
     * 计算当前日期到目标日期d1的月份差
     *
     * @param d1 Date类型的日期比较对象
     * @return 月份差值
     */
    public static int calculateMonthToNow(Date d1) {
        return calculateMonthOfTwoDate(d1, new Date());
    }

    /**
     * 计算当前日期到目标日期d1的天数差
     *
     * @param d1 Date类型的日期比较对象
     * @return 天数差值
     */
    public static int calculateDayToNow(Date d1) {
        return calculateDayOfTwoDate(d1, new Date());
    }

    /**
     * 计算日期d2和日期d1的年数差
     *
     * @param d1 Date类型的日期比较对象1
     * @param d2 Date类型的日期比较对象2
     * @return d2-d1的年数差值
     */
    public static int calculateYearOfTwoDate(Date d1, Date d2) {
        int month = calculateMonthOfTwoDate(d1, d2);
        return month / 12;
    }

    /**
     * 计算d2和d1的秒数差
     *
     * @param d1 Date类型的日期比较对象1
     * @param d2 Date类型的日期比较对象2
     * @return d2-d1的秒数差值
     */
    public static long calculateSecondsOfTwoDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("arguments can't be null");
        }
        Duration between = Duration.between(d1.toInstant(), d2.toInstant());
        return between.getSeconds();
    }

    /**
     * 计算日期d2和日期d1的月份差
     *
     * @param d1 Date类型的日期比较对象1
     * @param d2 Date类型的日期比较对象2
     * @return d2-d1的月份差值
     */
    public static int calculateMonthOfTwoDate(Date d1, Date d2) {
        int days = calculateDayOfTwoDate(d1, d2);
        return days / 30;
    }

    /**
     * 计算日期d2和日期d1的天数差
     *
     * @param d1 Date类型的日期比较对象1
     * @param d2 Date类型的日期比较对象2
     * @return d2-d1的天数差值
     */
    public static int calculateDayOfTwoDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("arguments can't be null");
        }
        LocalDate ld1 = toLocalDate(d1);
        LocalDate ld2 = toLocalDate(d2);
        return (int) ChronoUnit.DAYS.between(ld1, ld2);
    }

    /**
     * 判断两个时间是否同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDay(Date d1, Date d2) {
        return equals(getDateTime("yyyy-MM-dd", d1),
                getDateTime("yyyy-MM-dd", d2));
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        } else {
            return str1.equals(str2);
        }
    }

    public static String getDateTime(String aMask, Date aDate) {
        if (aDate == null) {
            return "";
        } else {
            SimpleDateFormat df = new SimpleDateFormat(aMask);
            return df.format(aDate);
        }
    }

    /**
     * 根据基准边界日期计算出指定日期
     *
     * @param boundary 基准边界日期
     * @param unit     计算单位：年、月、日、周
     * @param num      跨度数量：大于0，往前加num个单位；小于0，往后减num个单位
     * @return 计算得到的最后指定日期
     * @see EnumDateUnit
     */
    public static Date getSpecificDay(LocalDate boundary, EnumDateUnit unit, long num) {
        if (boundary == null || num == 0)
            throw new IllegalArgumentException("LocalDateUtils.addMonths parameters is unvalid");
        LocalDate result;
        switch (unit) {
            case DAY:
                result = num > 0 ? boundary.plusDays(num) : boundary.minusDays(num);
                return toDate(result);
            case YEAR:
                result = num > 0 ? boundary.plusYears(num) : boundary.minusYears(num);
                return toDate(result);
            case MONTH:
                result = num > 0 ? boundary.plusMonths(num) : boundary.minusMonths(num);
                return toDate(result);
            case WEEK:
                result = num > 0 ? boundary.plusWeeks(num) : boundary.minusWeeks(num);
                return toDate(result);
        }
        return null;
    }

    /**
     * 根据基准边界日期计算出指定日期
     *
     * @param boundary 基准边界日期
     * @param unit     计算单位：年、月、日、周
     * @param num      跨度数量：大于0，往前加num个单位；小于0，往后减num个单位
     * @return 计算得到的最后指定日期
     * @see EnumDateUnit
     */
    public static Date getSpecificDay(Date boundary, EnumDateUnit unit, long num) {
        return getSpecificDay(toLocalDate(boundary), unit, num);
    }

    /**
     * 计算以今天为基准边界的指定日期
     *
     * @param unit 计算单位：年、月、日、周
     * @param num  跨度数量：大于0，往前加num个单位；小于0，往后减num个单位
     * @return 计算得到的最后指定日期
     * @see EnumDateUnit
     */
    public static Date getSpecificDay(EnumDateUnit unit, long num) {
        return getSpecificDay(LocalDate.now(), unit, num);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalDate转化为Date
     *
     * @param localDate
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将LocalDate转化为Date,并且+Seconds
     *
     * @param localDate
     * @param plusSeconds
     * @return
     */
    public static Date toDate(LocalDate localDate, long plusSeconds) {
        Date d = toDate(localDate);
        return new Date(d.getTime() + (plusSeconds * 1000));
    }

    /**
     * Date类型转换成JDK8的LocalDate（LocalDate只保存Date的年月日）
     *
     * @param date Date类型对象入参
     * @return LocalDate类型对象返回
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date类型转换成JDK8的LocalTime（LocalTime只保存Date的时分秒）
     *
     * @param date Date类型对象入参
     * @return LocalTime类型对象返回
     */
    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * Date类型转换成JDK8的LocalDateTime（LocalDateTime保存Date的年月日时分秒）
     *
     * @param date Date类型对象入参
     * @return LocalDateTime类型对象返回
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 取精确到月份的字符串
     */
    public static String getMonthDateString(Date date) {
        if (date == null)
            return "";
        return getMonthDateString(toLocalDate(date));
    }

    /**
     * 取精确到日的字符串
     */
    public static String format(Date date) {
        if (date == null)
            return "";
        return format(toLocalDate(date));
    }

    /**
     * 取精确到月份的字符串
     */
    public static String getMonthDateString(LocalDate date) {
        if (date == null)
            return "";
        return date.format(defaultMonthFormatter);
    }

    /**
     * 取精确到日的字符串
     */
    public static String format(LocalDate date) {
        if (date == null)
            return "";
        return date.format(defaultDateFormatter);
    }

    /**
     * 根据格式化表达式格式化字符串
     *
     * @param date
     * @param formatPattern
     */
    public static String format(LocalDate date, String formatPattern) {
        if (date == null)
            return "";
        return date.format(DateTimeFormatter.ofPattern(formatPattern));
    }

    /**
     * <p>
     * 计算周期内的总天数
     * <p>
     * <p>
     * 1. 以今天为截止日期，取当月的总天数
     * <p>
     * <p>
     * 2. 循环操作：今天往前一个月，累加上个月的天数
     * <p>
     * <p>
     * 3. 循环次数：周期月份数（常量，默认是6个月）-1次
     * </p>
     *
     * @return 周期内的总天数
     */
    public static int calculateTotalDays(int billMonthQuantity) {
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();
        int totalDays = currentDay;
        for (int i = 0; i < billMonthQuantity - 1; i++) {
            today = today.minusMonths(1);
            totalDays += today.lengthOfMonth();
        }
        return totalDays;
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        Date d2 = cal.getTime();
        cal.set(1984, Calendar.AUGUST, 19, 0, 0, 0);
        Date d1 = cal.getTime();
        System.out.println(calculateMonthOfTwoDate(d1, d2));
        System.out.println(calculateYearOfTwoDate(d1, d2));
        System.out.println(calculateDayOfTwoDate(d1, d2));
        System.out.println(LocalDate.now().toEpochDay());
        System.out.println(toLocalDate(new Date()));
        System.out.println(getYmdDay(new Date()));
    }

    /**
     * 比较两个日期是否同一个月
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        LocalDate firstDayThisMonth1 = toLocalDate(date1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDayThisMonth2 = toLocalDate(date2).with(TemporalAdjusters.firstDayOfMonth());
        return firstDayThisMonth1.compareTo(firstDayThisMonth2) == 0;
    }

    /**
     * 比较日期是否和当前日期在同一个月份
     */
    public static boolean isCurrentMonth(Date date1) {
        return isSameMonth(date1, new Date());
    }

    /**
     * 得到当前日期yyyyMMdd格式
     *
     * @return
     */
    public static int getNowDayInteger() {
        LocalDate now = LocalDate.now();
        int back = now.getYear() * 10000;
        back += (now.getMonthValue() * 100);
        back += now.getDayOfMonth();
        return back;
    }

    /**
     * 获取n天后零点的date
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getNextDayZero(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static int getYmdDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getYmdDay(LocalDate d) {
        return d.getYear() * 10000 + (d.getMonthValue() * 100) + (d.getDayOfMonth());
    }

    public static String getPrettyDateString(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("+8")).format(
                Constants.prettyDateTimeFormatter);
    }

    /**
     * 字符串转换成date
     * @param date
     * @return
     */
    public static Date getDateFromStr(String date){

        try {
            return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

}
