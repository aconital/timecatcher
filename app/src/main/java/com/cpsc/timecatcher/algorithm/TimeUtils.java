package com.cpsc.timecatcher.algorithm;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yutongluo on 2/6/16.
 */
public class TimeUtils {
    // Determine if two dates overlap
    public static boolean isOverlap(Date startA, Date endA, Date startB, Date endB) {
        return startA.before(endB) && startB.before(endA);
    }

    public static Date addMinutesToDate(Date date, int minutes) {
        final long MIN_IN_MS = 60000;
        long dateMs = date.getTime();
        return new Date(dateMs + minutes * MIN_IN_MS);

    }

    public static Calendar addMinutesToDate(Calendar c, int minutes) {
        final long MIN_IN_MS = 60000;
        long dateMs = c.getTimeInMillis();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(dateMs + minutes * MIN_IN_MS);
        return newCalendar;

    }

    public static int getMinutesDiff(Date startTime, Date endTime) {
        if( startTime == null || endTime == null ) return 0;
        else {
            return (int)((endTime.getTime() / 60000) - (startTime.getTime()/60000));
        }
    }
}
