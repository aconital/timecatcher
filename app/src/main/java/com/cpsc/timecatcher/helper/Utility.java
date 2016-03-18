package com.cpsc.timecatcher.helper;

import com.algorithm.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hroshandel on 2016-02-18.
 */
public  class Utility {
    private static final Calendar calendar= Calendar.getInstance();
    public static long getTodayLong()
    {
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);
        long todayLong= calendar.getTime().getTime();
        return todayLong;
    }

    public static Time dateToTime(Date date) {
        calendar.setTime(date);
        return new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static Date timeToDate(Date date, Time time) {
        calendar.setTime(date);
        int hour = time.getHour();
        int minute = time.getMinute();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

}
