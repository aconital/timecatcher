package com.algorithm;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestJunit {   
		
   @Test
   public void testTime(){
	   int hour=1;
	   int minute =30;
	   Time time= new Time(hour,minute);
	   
	   System.out.println("Test Time Class: "); 
	   assertEquals(hour, time.getHour()); 
	   assertEquals(minute, time.getMinute()); 
	   
	   time.setHour(2);
	   time.setMinute(15);
	   assertEquals(2, time.getHour()); 
	   assertEquals(15, time.getMinute()); 
	   
	   Time t1=new Time(1,10);
	   Time t2=new Time(2,10);
	   
	   assertEquals(0, t1.compareTime(t1)); // t1 == t1
	   assertEquals(1, t2.compareTime(t1)); // t2>t1
	   assertEquals(-1, t1.compareTime(t2)); // t1< t2
	   
	   Time t3=new Time(1,10);
	   Time t4=new Time(1,15);

	   assertEquals(-1, t3.compareTime(t4)); // t2 < t4
	   assertEquals(1, t4.compareTime(t3)); //  t4> t3   
   }
   
   @Test
   public void testTimeSlice(){
		Time startTime=new Time(1,10);
		Time endTime=new Time(1,45);
		TimeSlice slice= new TimeSlice(startTime, endTime);
		
		assertEquals(startTime,slice.getStartTime() );
		assertEquals(endTime,slice.getEndTime() );
		
		startTime=new Time(2,10);
		endTime=new Time(3,25);
		slice.setTimeSlice(startTime,endTime);
		assertEquals(startTime,slice.getStartTime() );
		assertEquals(endTime,slice.getEndTime() );
		
		startTime=new Time(1,10);
		endTime=new Time(1,25);
		TimeSlice slice1= new TimeSlice(startTime, endTime);
		assertEquals(1,slice1.isBefore(slice) );
		assertEquals(-1,slice.isBefore(slice1) );
   }
   
   @Test 
   public void testDomain(){
		Time startTime=new Time(1,10);
		Time endTime=new Time(1,45);
		TimeSlice slice= new TimeSlice(startTime, endTime);
		boolean available=false;
		Domain d= new Domain(slice,available);
		
		assertEquals(slice, d.getTimeSlice());
		assertEquals(available, d.getAvailable());
		
		endTime.setHour(3);
		slice.setTimeSlice(startTime, endTime);
		d.setTimeSlice(slice);
		d.setAvailable(true);
		assertEquals(slice, d.getTimeSlice());
		assertEquals(true, d.getAvailable());
   }
   
   @Test 
   public void testTask(){
	   Time time=new Time(1,10);// 1h and 10 minutes
	   Task task=new FlexibleTask(time);
	   //assertEquals(time, task.getEstimatedTime());
   }
}

