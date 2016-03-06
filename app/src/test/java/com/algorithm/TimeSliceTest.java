package com.algorithm;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by fujiaoyang1 on 3/6/16.
 */
public class TimeSliceTest {
    private TimeSlice slice;
    private Time startTime;
    private Time endTime;
    private boolean available;
    @Before
    public void setUp() throws Exception {
        startTime=new Time(1,10);
        endTime=new Time(1,45);
        available=true;
        slice= new TimeSlice(startTime,endTime,available);
    }
    @Test
    public void testGetStartTime() throws Exception {
        assertEquals(startTime,slice.getStartTime());
    }

    @Test
    public void testGetEndTime() throws Exception {
        assertEquals(endTime,slice.getEndTime());
    }

    @Test
    public void testGetAvailable() throws Exception {
        assertEquals(available,slice.getAvailable());
    }

    @Test
    public void testSetAvailable() throws Exception {
        slice.setAvailable(false);
        assertEquals(false, slice.getAvailable());
    }

    @Test
    public void testSetTimeSlice() throws Exception {
        startTime=new Time(2,50);
        endTime=new Time(4,25);
        slice.setTimeSlice(startTime,endTime);
        assertEquals(startTime, slice.getStartTime());
        assertEquals(endTime,slice.getEndTime());
    }

    @Test
    public void testIsBefore() throws Exception {
        TimeSlice slice1= new TimeSlice(new Time(1,30),new Time(2,40),true);
        TimeSlice slice2= new TimeSlice(new Time(1,45),new Time(2,40),true);
        TimeSlice slice3= new TimeSlice(new Time(2,45),new Time(3,40),true);
        assertEquals(false, slice.isBefore(slice1));
        assertEquals(true  ,slice.isBefore(slice2));
        assertEquals(true  ,slice.isBefore(slice3));
    }

    @Test
    public void testIsOverlap() throws Exception {
        TimeSlice slice1= new TimeSlice(new Time(1,30),new Time(2,40),true);
        TimeSlice slice2= new TimeSlice(new Time(1,45),new Time(2,40),true);
        TimeSlice slice3= new TimeSlice(new Time(2,45),new Time(3,40),true);

        assertEquals(true  ,slice.isOverlap(slice1));
        assertEquals(true  ,slice1.isOverlap(slice));
        assertEquals(false   ,slice.isOverlap(slice2));
        assertEquals(false   ,slice.isOverlap(slice3));
    }

    @Test
    public void testEquals() throws Exception {
        TimeSlice slice1= new TimeSlice(new Time(1,10),new Time(1,45),true);
        TimeSlice slice2= new TimeSlice(new Time(1,30),new Time(2,40),true);

        assertEquals(true  ,slice.equals(slice1));
        assertEquals(false  ,slice.equals(slice2));
    }

    @Test
    public void testCompareTo() throws Exception {
        TimeSlice slice1= new TimeSlice(new Time(1,10),new Time(1,45),true);
        TimeSlice slice2= new TimeSlice(new Time(1,30),new Time(2,40),true);
        TimeSlice slice3= new TimeSlice(new Time(0,30),new Time(0,40),true);

        ArrayList<TimeSlice> sliceArrayList1=new ArrayList<TimeSlice>();
        ArrayList<TimeSlice> sliceArrayList2=new ArrayList<TimeSlice>();
        sliceArrayList1.add(slice1);
        sliceArrayList1.add(slice2);
        sliceArrayList1.add(slice3);

        sliceArrayList2.add(slice3);
        sliceArrayList2.add(slice1);
        sliceArrayList2.add(slice2);

        //Ascending order
        Collections.sort(sliceArrayList1);

        for(int i=0;i<3;i++){
            slice1= sliceArrayList1.get(i);
            slice2= sliceArrayList2.get(i);
            assertEquals(true,slice1.equals(slice2));
        }
    }
}