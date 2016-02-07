package com.cpsc.timecatcher.algorithm;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yutongluo on 2/6/16.
 */
public class TimeUtilsTest extends TestCase {

    public void testIsOverlap() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date startA, endA, startB, endB;

        // Test overlap times on the same day
        calendar.set(Calendar.DATE, 1);

        // BASIC CASE
        // A starts at 10:30 AM, ends at 11:00
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        startA = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        endA = calendar.getTime();

        // B starts at 11:00, ends at 11:30
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        startB = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        endB = calendar.getTime();

        assertEquals(false, TimeUtils.isOverlap(startA, endA, startB, endB));

        // B starts at 10:59 should be overlapping
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 59);
        startB = calendar.getTime();
        assertEquals(true, TimeUtils.isOverlap(startA, endA, startB, endB));

        // reverse should also overlap
        assertEquals(true, TimeUtils.isOverlap(startB, endB, startA, endA));


        // COMPLETE OVERLAP
        // A starts at 10:30 AM, ends at 11:00
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 30);
        startA = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        endA = calendar.getTime();

        // B starts at 10:00, ends at 11:30
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        startB = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        endB = calendar.getTime();
        assertEquals(true, TimeUtils.isOverlap(startA, endA, startB, endB));

        // DIFFERENT DATES SHOULD NEVER OVERLAP
        calendar.set(Calendar.DATE, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 30);
        startA = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        endA = calendar.getTime();
        assertEquals(false, TimeUtils.isOverlap(startA, endA, startB, endB));
    }
}