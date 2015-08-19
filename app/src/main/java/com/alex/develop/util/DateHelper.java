package com.alex.develop.util;

import com.alex.develop.entity.Enum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex on 15-7-19.
 * 日期处理类
 */
public class DateHelper {

    /**
     * 获取今天的日期
     * @return eg:“20150719”
     */
    public static String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        return sdf.format(new Date());
    }

    /**
     *
     * @return
     */
    public static Enum.Month month() {
        int ordinal = Calendar.getInstance().get(Calendar.MONTH);
        Enum.Month month = Enum.Month.build(ordinal);
        return month;
    }

    /**
     * 计算距离今天dayCount天之前或之后那天的日期
     * @param dayCount <0,表示dayCount天之前; >0 表示dayCount天之后
     * @return eg:"21050519"
     */
    public static String offset(int dayCount) {

        Calendar c = Calendar.getInstance();

        // year
        int year = c.get(Calendar.YEAR);

        // 闰年2月多一天
        if ((0 == year % 4 && 0 != year % 100) || 0 == year % 400) {
            days[1] = 29;
        } else {
            days[1] = 28;
        }

        // month(每月从0开始计数，即1实际上表示2月)
        int month = c.get(Calendar.MONTH);

        // day
        int day = c.get(Calendar.DATE);

        day += dayCount;

        while (true) {
            int dayOfMonth = days[month];
            if (0 < day && day <= dayOfMonth) {
                break;
            } else if (0 == day) {

                --month;
                // 考虑偏移的天数太多，跨年的情况
                if (-1 == month) {
                    month = days.length - 1;
                    --year;
                }

                day = days[month];
                break;
            } else if (dayOfMonth < day) {
                day -= dayOfMonth;

                ++month;
                if (days.length == month) {
                    month = 0;
                    ++year;

                    // 闰年2月多一天
                    if ((0 == year % 4 && 0 != year % 100) || 0 == year % 400) {
                        days[1] = 29;
                    } else {
                        days[1] = 28;
                    }
                }

            } else {
                --month;
                if (-1 == month) {
                    month = days.length - 1;
                    --year;

                    // 闰年2月多一天
                    if ((0 == year % 4 && 0 != year % 100) || 0 == year % 400) {
                        days[1] = 29;
                    } else {
                        days[1] = 28;
                    }
                }
                day += dayOfMonth;
            }
        }

        return String.format("%d%02d%02d", year, month + 1, day);
    }

    /**
     * 计算{types}所指定的所有月份表示的日期区间
     * @param types 有二个参数，p1为{Enum.Month}类型，指明要查询的是从几月起之前的数据，具体查询多少，需要根据p2做出判断；p2为{Enum.Period}类型，指明需要的数据周期，是日，周，月
     * @return 需要查询的日期区间
     */
    public static String[] getDateScope(Enum.EnumType... types) {

        // 参数不合法的情况，程序将报错
        if(2 != types.length) {
            throw new IllegalArgumentException();
        }

        String[] data = new String[2];

        Enum.Month month = (Enum.Month) types[0];


        Enum.Period period = (Enum.Period) types[1];


        data[0] = "20150709";
        data[1] = today();

        return data;
    }

    private static int[] days = {
            31,// 1月
            28,// 2月
            31,// 3月
            30,// 4月
            31,// 5月
            30,// 6月
            31,// 7月
            31,// 8月
            30,// 9月
            31,// 10月
            30,// 11月
            31 // 12月
    };

    private DateHelper() {
    }
}