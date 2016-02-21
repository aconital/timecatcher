package com.cpsc.timecatcher.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hroshandel on 2016-02-18.
 */
public  class Utility {
    public static long getTodayLong()
    {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND,0);
        today.set(Calendar.MILLISECOND, 0);
        long todayLong= today.getTime().getTime();
        return todayLong;
    }

}
