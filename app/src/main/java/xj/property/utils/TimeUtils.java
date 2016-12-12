package xj.property.utils;


import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import xj.property.utils.other.PreferencesUtil;

/**
 * Created by maxwell on 15/2/9.
 */
public class TimeUtils {
    /**
     * logger
      */

    public static String fromLongToString(String number){
        long startT = fromDateStringToLong("1970-01-01 08:00:00");
        long in = Long.parseLong(number);
        //输入的是相对时间的 秒差
        Date date1 = new Date(1000*in+startT);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return inputFormat.format(date1);
    }
    public static String fromLongToString2(String number){
        long startT = fromDateStringToLong("1970-01-01 08:00:00");
        long in = Long.parseLong(number);
        //输入的是相对时间的 秒差
        Date date1 = new Date(1000*in+startT);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        return inputFormat.format(date1);
    }


    //date conversion
    public static long fromDateStringToLong(String inVal)
    {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime(); //返回毫秒数
    }


    //date conversion
    public static int fromDateStringToInt(String inVal)
    {
        Date date = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) (date.getTime()/1000); //返回毫秒数
    }

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    //// 判断两个事件是否为同一天
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {

        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }
    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }



    /**
     * 是否为今天
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH)+1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if(year1 == year2 && month1 == month2 && day1 == day2){
            return true;
        }
        return false;
    }

    /**
     * 是否为昨天
     * @param date
     * @return
     */
    public static boolean isYesteday(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH)+1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if(year1 == year2 && month1 == month2 && day1 == (day2-1)){
            return true;
        }
        return false;
    }


    /**
     * 是否为上一周
     * @param date
     * @return
     */
    public static boolean isLastWeek(Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int week1 = c1.get(Calendar.WEEK_OF_MONTH);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH)+1;
        int week2=  c2.get(Calendar.WEEK_OF_MONTH);
        if(year1 == year2 && month1 == month2 && week1 == (week2-1)){

            return true;
        }
        return false;
    }


    /**
     *
     * @param context
     * @return
     */
    public static boolean isNeedShowRPTopList(Context context){
        Date currentTime = new Date();

        long lastReadTime = PreferencesUtil.getReadRPTopListTime(context);
        /// 初次安装
        if(lastReadTime<=0){
            PreferencesUtil.saveReadRPTopListTime(context,currentTime.getTime());
            return true;
        }

        Date lastReadDate = new Date(lastReadTime);

        float gapCount = getGapCount(lastReadDate,currentTime);

        //// 如果大于等于一周,就显示
        if(gapCount>=7.0f){

            PreferencesUtil.saveReadRPTopListTime(context,currentTime.getTime());
            return true;
        }else{

            return false;
        }
    }





    /**
     * 获取两个日期之间的间隔天数
     * @return
     */
    public static float getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (float) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }






}
