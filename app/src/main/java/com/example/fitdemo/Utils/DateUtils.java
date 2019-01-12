package com.example.fitdemo.Utils;

import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by 最美人间四月天 on 2018/12/5.
 */

public class DateUtils {

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    private static String mHour;
    private static String mMin;

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        mMin= String.valueOf(c.get(Calendar.MINUTE));
        return mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMin;
    }

    public static String StringWeek(int week){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +week);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }

    public static String StringTime(int week){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +week);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay + " " + mMonth + "-" + mDay;
    }

    public static int IntWeek(int week){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +week);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int re = 0;
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1 :{
                re = 6;
               break; 
            }
            case 2 :{
                re = 0;
                break;
            }
            case 3 :{
                re = 1;
                break;
            }
            case 4 :{
                re = 2;
                break;
            }
            case 5 :{
                re = 3;
                break;
            }
            case 6 :{
                re = 4;
                break;
            }
            case 7 :{
                re = 5;
                break;
            }
        }
        
        return re;
    }

    public static int IntTime(int week){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +week);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if(mMonth.length()==1){
            mMonth = "0" + mMonth;
        }
        if(mDay.length() == 1){
            mDay = "0" + mDay;
        }
        return Integer.parseInt(mYear + mMonth + mDay);
    }
}
