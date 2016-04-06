package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class TimeTest {
    private Time time;
    @Before
    public void setUp() throws Exception {
        //System.out.println("TimeTest Result: ");
        time= new Time(1,30);
    }

    @Test
    public void testGetHour() throws Exception {
        assertEquals(1, time.getHour());
    }

    @Test
    public void testGetMinute() throws Exception {
        assertEquals(30, time.getMinute());
    }

    @Test
    public void testSetHour() throws Exception {
        time.setHour(2);
        assertEquals(2, time.getHour());

    }

    @Test
    public void testSetMinute() throws Exception {
        time.setMinute(15);
        assertEquals(15, time.getMinute());
    }

    @Test
    public void testAddTime() throws Exception {
        Time t1=new Time(1,45);
        Time t2=new Time(1,15);
        assertEquals(new Time(3,0),t1.addTime(t2));

        t2=new Time(2,10);
        assertEquals(new Time(3,55),t1.addTime(t2));
    }

    @Test
    public void testSubstractTime() throws Exception {
        assertEquals(new Time(0,30),new Time(1,45).subtractTime(new Time(1, 15)));
        assertEquals(new Time(2,0),new Time(9,0).subtractTime(new Time(7, 0)));
    }

    @Test
    public void testGetTimeString() throws Exception {
        assertEquals("2:15",new Time(2,15).getTimeString());
        assertEquals("12:00",new Time(12,0).getTimeString());
    }

    @Test
    public void testCompareTime() throws Exception {
        Time t1=new Time(1,10);
        Time t2=new Time(2,25);
        assertEquals(0,t1.compareTime(t1));
        assertEquals(-1,t1.compareTime(t2));
        assertEquals(1,t2.compareTime(t1));
    }

    @Test
    public void testEqual () throws Exception {
        Time t1=new Time(1,10);
        Time t2=new Time(1,10);
        Time t3=new Time(2,10);

        assertEquals(true ,t1.equals(t2));
        assertEquals(false ,t1.equals(t3));

    }
}