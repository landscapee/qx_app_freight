package qx.app.freight.qxappfreight.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 获取系统时间值
 * Created by ouyangbin on 2015/9/23.
 */
public class TimeUtils {

    /**
     *获取当前时间
     *
     * @return
     */
    public static Long getTime() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        return date.getTime();
    }

    /**
     * 相差几天
     *
     * @return
     */
    public static String getMinToDay(int time) {
        int day = 0;
        int hour = 0;
        int min = 0;
        day = (int) time/(24*60);
        hour = (int)(time - (day*24*60))/60;
        min = time - (day*24*60) - (hour*60);
        String date = "";
        if (day > 0)
            date = date+day+"d";
        if (hour > 0)
            date = date+hour+"h";
        if (min > 0)
            date = date+min+"m";
        if (day == 0&& hour == 0&&min == 0)
            date = "<1m";
        return date;
    }

    /**
     * yyyy年MM月dd日 HH时mm分ss秒 星期几
     *
     * @return
     */
    public static String getTime1() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEEE");
        Log.e("time", "time1=" + format.format(date));
        return format.format(date);
    }

    /**
     * 年-月-日 时:分:秒
     *
     * @return
     */
    public static String getTime2() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("time", "time2=" + format.format(date));
        return format.format(date);
    }

    /**
     * 年-月-日 时:分:秒
     *
     * @return
     */
    public static String getTime2(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("time", "time2=" + format.format(date));
        return format.format(date);
    }

    /**
     * 年-月-日 T 时:分:秒:毫秒
     *
     * @return
     */
    public static String getTimeT() {
        StringBuilder builder = new StringBuilder();
        builder.append(getTime2_1());
        builder.append("T");
        builder.append(getTime4_1());

        return builder.toString();
    }

    /**
     * 年-月-日
     *
     * @return
     */
    public static String getTime2_1() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("time", "time2=" + format.format(date));
        return format.format(date);
    }

    /**
     * 将时间戳转为年月日
     * @param time 毫秒数
     * @return 年-月-日
     */
    public static String getTime2_1(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("time", "time2=" + format.format(date));
        return format.format(date);
    }

    /**
     * 年/月/日
     *
     * @return
     */
    public static String getTime3() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Log.e("time", "time3=" + format.format(date));
        return format.format(date);
    }

    /**
     * 时:分:秒
     *
     * @return
     */
    public static String getTime4() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Log.e("time", "time4=" + format.format(date));
        return format.format(date);
    }

    public static String getTime4_1() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        Log.e("time", "time4=" + format.format(date));
        return format.format(date);
    }

    /**
     * 星期几
     *
     * @return
     */
    public static String getTime5() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        Log.e("time", "time5=" + format.format(date));
        return format.format(date);
    }

    /**
     * 周几
     *
     * @return
     */
    public static String getTime6() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("E");
        Log.e("time", "time6=" + format.format(date));
        return format.format(date);
    }

    /**
     * 时间戳转换成4位数时间
     *
     * HHmm
     * @param second
     * @return
     */
    public static String date2Tasktime(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HHmm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//时间转换统一用东八区
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳转换成4位数时间
     *
     * @return
     */
    public static String com2time(long time1, long time2) {
        if (time1 == 0 || time2 == 0) {
            return "0";
        }
        String seconds = (time1 - time2) / 1000 + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HHmm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }


    /**
     * 判断是时间是否为空
     *
     * @param second
     * @return
     */
    public static boolean isTimeEmpty(long second) {
        if (second == 0)
            return true;
        else
            return false;
    }

    /**
     * 时间戳转换成4位数时间
     *
     * HH:mm
     * @param second 时分间带冒号
     * @return
     */
    public static String date2Tasktime3(Long second) {

        if (second == null||second <= 0) {
            return "--:--";
        }
        String seconds = (second / 1000) + "";
        if (seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 时间戳转换成6位数时间
     *
     * @param second 时分秒间带冒号
     * @return
     */
    public static String date2Tasktime4(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳转换成6位数时间
     *
     * @param second 时分秒间不带冒号
     * @return
     */
    public static String date2Tasktime5(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }
    /**
     * 时间戳转换成6位数时间
     *
     * @param second 时分秒间不带冒号
     * @return
     */
    public static String date2Tasktime6(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳转换成4位数时间
     *
     * @param second
     * @return
     */
    public static String date2day(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳转换成6位数时间
     *
     * @param second
     * @return
     */
    public static String date2Tasktime2(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "ddHHmm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳获取日
     *
     * @param second
     * @return
     */
    public static String getDay(Long second) {
        if (second == null||second <= 0) {
            return "-";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }
    /**
     * 时间戳获取12:34(05)
     *
     * @param second
     * @return
     */
    public static String getHMDay(long second) {
        if (second <= 0) {
            return "";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HH:mm(dd)";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }


    /**
     * 时间戳获取时
     *
     * @param second
     * @return
     */
    public static String getHour(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }


    /**
     * 时间戳获取中文日期时
     *
     * @param second
     * @return
     */
    public static String getDateCH(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "dd日HH时";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * TODO: 时间戳转 分:秒
     *
     * @param time1 单位：秒
     * @return
     */
    public static String date2EptTime(long time1) {
        if (time1 <= 0) {
            return "00:00";
        }
        int time = (int) (time1);
        String timeStr = null;
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 时间戳转换成4位数时间
     *
     * @param second
     * @return
     */
    public static String datetimeTo4(long second) {
        if (second <= 0) {
            return "-";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "HHmm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }
    /**
     * 时间戳转换成年月日
     *
     * @param second
     * @return
     */
    public static String date2time(long second) {
        if (second <= 0) {
            return "-";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }
    /**
     * 年-月-日
     *
     * @param second
     * @return
     */
    public static String date3time(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }

    /**
     * 时间戳转换成日 时分
     *
     * @param second
     * @return
     */
    public static String dateDayHourMin(long second) {
        if (second <= 0) {
            return "0";
        }
        String seconds = (second / 1000) + "";
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        String format = "dd日 HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String re = sdf.format(new Date(Long.valueOf(seconds + "000")));
        return re;
    }


    /**
     * 计算两个时间戳之间相差分钟数
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDuration(long start, long end) {
        int duration = (int) (start - end) / (1000 * 60);
        return duration;
    }

}
